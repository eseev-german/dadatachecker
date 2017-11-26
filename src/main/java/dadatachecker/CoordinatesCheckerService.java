package dadatachecker;

import dadatachecker.entity.Address;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CoordinatesCheckerService {
    private final static String SOURCE_FILE_PATH = "sample.txt";
    private final static String TARGET_FILE_PATH = "report.txt";
    private final FileService fileService;
    private final DadataService dadataService;

    public CoordinatesCheckerService() {
        dadataService = new DadataService();
        fileService = new FileService();
    }

    public void check() {
        List<String> report = fileService.getAddressesFromFile(SOURCE_FILE_PATH)
                .stream()
                .map(
                        address -> {
                            Address response = dadataService.getAddressSuggestions(address.getName()).get(0);
                            double distance = address.getData().distanceToPoint(response.getData());
                            if (distance > 0.1) {
                                return getReportLine(address, response, distance);
                            }
                            return "";
                        }
                ).filter(s -> !s.isEmpty())
                .collect(toList());
        if (!report.isEmpty()) {
            Path reportFilePath = Paths.get(TARGET_FILE_PATH);
            try {
                Files.deleteIfExists(reportFilePath);
            } catch (IOException e) {
                System.err.println("Error during old report file deletion");
                e.printStackTrace();
            }

            try {
                Files.createFile(reportFilePath);
            } catch (IOException e) {
                System.err.println("Error during report file creation!");
                e.printStackTrace();
            }
            try {
                Files.write(reportFilePath, report);
            } catch (IOException e) {
                System.err.println("Error during report file writing!");
                e.printStackTrace();
            }
        }
    }

    String getReportLine(Address source, Address response, double distance) {
        return source.getName() + " " + response.getData().getLatitude() + " " + response.getData().getLongitude()
                + " " + source.getData().getLatitude() + " " + source.getData().getLongitude() + " " + distance;
    }
}
