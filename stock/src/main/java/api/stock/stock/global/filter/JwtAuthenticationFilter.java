package api.stock.stock.global.filter;


import api.stock.stock.global.security.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //request가 들어왔을 떄 request Header의 authorization 필드의 bearer token값을 가져옴
    // 가져온 토큰을 검증하고 검증 결과를 securityContext에 추가


    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    public static JwtAuthenticationFilter of(TokenProvider tokenProvider, RedisTemplate redisTemplate){
        return new JwtAuthenticationFilter(tokenProvider,redisTemplate);
    }




    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = parseBearerToken(request);
        SetOperations<String,String> setOperations = redisTemplate.opsForSet();
        boolean isLogout = false;

        if (token != null && !token.equalsIgnoreCase("null")) {
            isLogout = Boolean.TRUE.equals(setOperations.isMember("Blacklist", token));
        }

        try {
            if(isLogout){
                response.addHeader("data","Blacklist");
            }
            else if (token != null && !token.equalsIgnoreCase("null")) {
                // 토큰을 검증하여 payload의 userEmail 가져옴
                String userEmail = tokenProvider.validate(token);

                // 만료된 AccessToken이라면 Refresh Token을 이용하여 새로운 AccessToken 발급
                if (userEmail == null) {
                    String refreshToken = request.getHeader("refreshToken");
                    if (refreshToken != null) {
                        String newAccessToken = tokenProvider.createAccessTokenFromRefreshToken(refreshToken);

                        if (newAccessToken != null && !newAccessToken.equals("Not Authenticated User!")) {
                            userEmail = tokenProvider.validate(newAccessToken);
                            // 새로 발급한 AccessToken으로 SecurityContext를 업데이트
                            AbstractAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userEmail, null, AuthorityUtils.NO_AUTHORITIES);
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                            securityContext.setAuthentication(authentication);
                            SecurityContextHolder.setContext(securityContext);

                            // 새로운 AccessToken을 응답 헤더에 추가
                            response.addHeader("newAccessToken", newAccessToken);
                        }
                    }
                } else {
                    // 만료되지 않은 AccessToken이라면 SecurityContext에 추가
                    AbstractAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userEmail, null, AuthorityUtils.NO_AUTHORITIES);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        } catch (Exception e){
            if(isLogout){
                response.addHeader("data","Blacklist");
            }
            else{
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }

    // request Header의 authorization 필드의 bearer token값을 가져오는 메서드
    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
