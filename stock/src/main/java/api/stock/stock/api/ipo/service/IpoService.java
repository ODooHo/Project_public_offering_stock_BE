package api.stock.stock.api.ipo.service;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.repository.IpoRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class IpoService {
    private final IpoRepository ipoRepository;


    public IpoService(IpoRepository ipoRepository) {
        this.ipoRepository = ipoRepository;
    }

    public ResponseDto<IpoEntity> getIpo(String ipoName) {
        IpoEntity ipo = ipoRepository.findByIpoName(ipoName);
        if (ipo == null) {
            throw new IPOApplicationException(ErrorCode.IPO_NOT_FOUND, String.format("Ipo name is %s", ipoName));
        }
        return ResponseDto.setSuccess("Success", ipo);
    }


    public ResponseDto<List<IpoEntity>> getIpoList() {
        return ResponseDto.setSuccess("Success", ipoRepository.findByOrderByDateDesc());
    }

    public List<IpoEntity> findIpoByName(List<String> ipoList) {
        return ipoRepository.findAllByIpoNameIn(ipoList);
    }

    public List<IpoEntity> searchIpo(String searchWord) {
        return ipoRepository.findByIpoNameContains(searchWord);
    }
}
