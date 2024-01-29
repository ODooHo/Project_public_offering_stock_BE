package api.stock.stock.api.ipo;

import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class IpoService {
    private final IpoRepository ipoRepository;



    public IpoService(IpoRepository ipoRepository) {
        this.ipoRepository = ipoRepository;
    }

    public ResponseDto<IpoEntity> getIpo(String ipoName){
        IpoEntity result = new IpoEntity();
        try{
            result = ipoRepository.findByIpoName(ipoName);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    return ResponseDto.setSuccess("Success", result);
    }


    public ResponseDto<List<IpoEntity>> getIpoList(){
        List<IpoEntity> result = new ArrayList<>();

        try{
            result = ipoRepository.findByOrderByDateDesc();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success",result);
    }

    public List<IpoEntity> findIpoByName(List<String> ipoList){
        List<IpoEntity> result = new ArrayList<>();
        try{
            result = ipoRepository.findAllByIpoNameIn(ipoList);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<IpoEntity> searchIpo(String searchWord){
        List<IpoEntity> result = new ArrayList<>();
        try{
            result = ipoRepository.findByIpoNameContains(searchWord);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }


}
