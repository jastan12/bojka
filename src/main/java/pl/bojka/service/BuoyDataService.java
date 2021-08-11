package pl.bojka.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bojka.model.BuoyDTO;
import pl.bojka.model.repository.DataRepo;

import java.util.Collections;
import java.util.List;

@Service
public class BuoyDataService {

    final DataRepo dataRepo;

    @Autowired
    public BuoyDataService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    public List<BuoyDTO> getDataToDisplay() {

        List<BuoyDTO> list = dataRepo.getRecordsList();
        if (list.isEmpty())
            return Collections.emptyList();
        else
            return list.subList(0,10);
    }
}
