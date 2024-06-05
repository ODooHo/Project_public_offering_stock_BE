package api.stock.stock.api.ipo.repository;

import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface IpoRepository extends MongoRepository<IpoEntity,Integer> {
    IpoEntity findByIpoName(String ipoName);
    List<IpoEntity> findAllByIpoNameIn(List<String> ipoNames);
    List<IpoEntity> findByOrderByDateDesc();

    List<IpoEntity> findByIpoNameContains(String ipoName);

}
