package api.stock.stock.api.ipo.favor;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
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
    public ResponseDto<List<IpoEntity>> getFavorList(String userEmail) {
        List<String> ipoList = favorRepository.findIpoNameByUserEmail(userEmail);
        List<IpoEntity> result = ipoService.findIpoByName(ipoList);
        return ResponseDto.setSuccess("Success", result);
    }

    public ResponseDto<FavorEntity> addFavor(FavorDto dto) {
        boolean isDuplicate = favorRepository.existsByIpoNameAndUserEmail(dto.getIpoName(), dto.getUserEmail());
        if (isDuplicate) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_FAVOR);
        }

        FavorEntity favor = modelMapper.map(dto, FavorEntity.class);
        favorRepository.save(favor);
        return ResponseDto.setSuccess("Success", favor);
    }


    public ResponseDto<Void> deleteFavor(String userEmail, String ipoName) {
        FavorEntity favor = favorRepository.findByIpoNameAndUserEmail(ipoName, userEmail);
        String favorUserEmail = favor.getUserEmail();

        if (!userEmail.equals(favorUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        favorRepository.deleteByIpoName(ipoName);
        return ResponseDto.setSuccess();
    }


    public void deleteByWithdraw(String userEmail) {
        favorRepository.deleteAllByUserEmail(userEmail);
    }
}
