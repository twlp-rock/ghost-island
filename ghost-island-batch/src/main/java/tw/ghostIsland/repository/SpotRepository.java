package tw.ghostIsland.repository;

import tw.ghostIsland.model.SpotEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpotRepository extends MongoRepository<SpotEntity, String> {
}
