package api.stock.stock.api.ipo.favor;

import api.stock.stock.api.ipo.IpoEntity;
import api.stock.stock.api.ipo.IpoService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FavorService {

    private final FavorRepository favorRepository;
    private final IpoService ipoService;
    private final ModelMapper modelMapper;

    @Autowired
    public FavorService(FavorRepository favorRepository, IpoService ipoService, ModelMapper modelMapper) {
        this.favorRepository = favorRepository;
        this.ipoService = ipoService;
        this.modelMapper = modelMapper;
    }


    @Transactional(readOnly = true)
    public ResponseDto<List<IpoEntity>> getFavorList(String userEmail){
        List<IpoEntity> result = new ArrayList<>();
        List<String> ipoList = favorRepository.findIpoNameByUserEmail(userEmail);
        try{
            result = ipoService.findIpoByName(ipoList);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success",result);
    }

    public ResponseDto<FavorEntity> addFavor(FavorDto dto){
        FavorEntity favor = modelMapper.map(dto,FavorEntity.class);
        try{
            String ipoName = favor.getIpoName();
            String userEmail = favor.getUserEmail();
            boolean isDuplicate = favorRepository.existsByIpoNameAndUserEmail(ipoName,userEmail);
            if (isDuplicate){
                return ResponseDto.setFailed("Already Favor");
            }
            favorRepository.save(favor);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success",favor);
    }


    public ResponseDto<String> deleteFavor(String userEmail, String ipoName){
        FavorEntity favor = favorRepository.findByIpoNameAndUserEmail(ipoName,userEmail);
        String favorUserEmail = favor.getUserEmail();

        if(!userEmail.equals(favorUserEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }

        try{
            favorRepository.deleteByIpoName(ipoName);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success","Delete Completed");
    }


    public void deleteByWithdraw(String userEmail){
        try{
            favorRepository.deleteAllByUserEmail(userEmail);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
