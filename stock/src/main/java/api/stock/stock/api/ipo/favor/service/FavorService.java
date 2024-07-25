package api.stock.stock.api.ipo.favor.service;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.ipo.domain.dto.IpoDto;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.service.IpoService;
import api.stock.stock.api.ipo.favor.domain.dto.FavorDto;
import api.stock.stock.api.ipo.favor.domain.entity.FavorEntity;
import api.stock.stock.api.ipo.favor.repository.FavorRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FavorService {

    private final FavorRepository favorRepository;
    private final IpoService ipoService;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<IpoDto> getFavorList(String userEmail) {
        List<String> ipoList = favorRepository.findIpoNameByUserEmail(userEmail);
        return ipoService.findIpoByName(ipoList);
    }

    public FavorDto addFavor(FavorDto dto) {
        boolean isDuplicate = favorRepository.existsByIpoNameAndUserEmail(dto.getIpoName(), dto.getUserEmail());
        if (isDuplicate) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_FAVOR);
        }

        FavorEntity favor = modelMapper.map(dto, FavorEntity.class);
        favorRepository.save(favor);
        return FavorDto.from(favor);
    }


    public void deleteFavor(String userEmail, String ipoName) {
        FavorEntity favor = favorRepository.findByIpoNameAndUserEmail(ipoName, userEmail);
        String favorUserEmail = favor.getUserEmail();

        if (!userEmail.equals(favorUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        favorRepository.deleteByIpoName(ipoName);
    }


    public void deleteByWithdraw(String userEmail) {
        favorRepository.deleteAllByUserEmail(userEmail);
    }
}
