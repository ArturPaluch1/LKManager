package LKManager.services.FilesService_unused;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PlikiService {

    public enum folder {
        terminarze,
        gracze
    }

    public File[] pobierzPlikiZFolderu(folder wybranyFolder) {
        File  folder = new File("Data/"+wybranyFolder);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                pobierzPlikiZFolderu(wybranyFolder);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
        return folder.listFiles();
    }
}
