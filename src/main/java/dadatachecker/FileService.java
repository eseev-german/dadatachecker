package dadatachecker;

import dadatachecker.entity.Address;
import dadatachecker.entity.Coordinates;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {

    public List<Address> getAddressesFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(filePath).toURI()))
                    .stream()
                    .map(line -> {
                        String[] splittedLine = line.split("\t");
                        String latitude = splittedLine[splittedLine.length - 2];
                        String longitude = splittedLine[splittedLine.length - 1];
                        String name = line.replaceFirst("\t" + longitude, "")
                                .replaceFirst("\t" + latitude, "");
                        return new Address(name, new Coordinates(latitude, longitude));
                    })
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error during parse " + filePath);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
