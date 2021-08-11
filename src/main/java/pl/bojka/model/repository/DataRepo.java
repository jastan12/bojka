package pl.bojka.model.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import org.decimal4j.util.DoubleRounder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.bojka.model.BuoyDTO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Component
@Data
@EnableScheduling
public class DataRepo {

    private static final Logger LOG = Logger.getLogger("loggerJan");
    private final double calib = 160.;
    private List<BuoyDTO> recordsList;
    private File file;
    private String fileName;

    public DataRepo(){
        this.fileName = "buoy3.csv";
        this.file = new File(this.fileName);
    }

    @Scheduled(fixedRate = 1800000)
    public void initialDataRead(){
        LOG.info("Downloading file... " + LocalDateTime.now().toString());
        try {

            URL url = new URL("http://qx.gry-giel.pl/buoy3.csv");
            InputStream in = url.openStream();
            Files.copy(in, Paths.get(fileName),REPLACE_EXISTING);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileReader fileReader = new FileReader(fileName);
            recordsList = new CsvToBeanBuilder(fileReader)
                    .withType(BuoyDTO.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
            Collections.reverse(recordsList);
            fileReader.close();
            formatTime();
            calculateOxygen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void formatTime(){
        recordsList.forEach(new Consumer<BuoyDTO>() {
            @Override
            public void accept(BuoyDTO buoyDTO) {
                StringBuilder dataTemp = new StringBuilder();
                dataTemp.append(buoyDTO.getMeasurementTime().substring(0, 14));
                dataTemp.replace(8,9," ");
                buoyDTO.setMeasurementTime(dataTemp.toString());
            }
        });
    }

    public void calculateOxygen(){
        recordsList.forEach(new Consumer<BuoyDTO>() {
            @Override
            public void accept(BuoyDTO buoyDTO) {
                double rawOxygen = buoyDTO.getOxygen();
                double temperature = buoyDTO.getTemperature();
                double oxPercent = (rawOxygen * 100) / calib;
                double oxygenMgPL = (14.1715218692999 * Math.exp( - 0.021571468824884 * temperature)) * (oxPercent / 100);
                buoyDTO.setOxygen(DoubleRounder.round(oxygenMgPL,2));
            }
        });
    }




//    @PostConstruct
//    public void method(){
//        LOG.info("Method called after construct");
//        LOG.warning("This is another color of log");
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void doSomething(){
//        System.err.println("Hello world from listener");
//    }
}
