package dev.asper.spark;

import lombok.val;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.util.MLWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public enum PipelineToBytesConverter {
    ;

    public static Path createFolder(String relPath) {
        Path path = Path.of(relPath);
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create directory for models. path=" + relPath + ", msg=" + e.getMessage());
        }
        return path;
    }

    public static byte[] toBytes(String tmpFolder, PipelineModel pipelineModel) {
        createFolder(tmpFolder);
        String modelFolderPath = tmpFolder + File.separator + UUID.randomUUID();
        String zipFile = modelFolderPath + ".zip";
        ZipFile file = new ZipFile(zipFile);
        try (file) {
            pipelineModel.write().overwrite().save(modelFolderPath);
            file.addFolder(new File(modelFolderPath));
            byte[] content = Files.readAllBytes(Path.of(zipFile));
            FileUtils.deleteDirectory(new File(tmpFolder));
            return content;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create archive, msg=" + e.getMessage());
        }
    }

    public static PipelineModel fromBytes(String folder, String modelName, byte[] dump) {
        String modelDataFolder = folder + File.separator + modelName;
        Path tmp = createFolder(modelDataFolder);
        File curFolder = tmp.toFile();
        Path zipFilePath = Path.of(tmp + File.separator + UUID.randomUUID() + ".zip");
        try {
            Files.write(zipFilePath, dump);
            ZipFile file = new ZipFile(zipFilePath.toFile());
            try (file) {
                file.extractAll(modelDataFolder);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't extract archive, msg=" + e.getMessage());
            }
            FileUtils.delete(zipFilePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("General IO exception, msg=" + e.getMessage());
        }
        File[] listOfFiles = curFolder.listFiles();
        if (listOfFiles != null && listOfFiles.length == 1 && listOfFiles[0].isDirectory()) {
            return PipelineModel.load(listOfFiles[0].getPath());
        } else {
            throw new RuntimeException("Not found directory containing model pipeline");
        }
    }

    public static Optional<PipelineModel> readFromFolder(String tmpFolder, String modelName) {
        try {
            Path path = Path.of(tmpFolder + File.separator + modelName);
            File file = path.toFile();
            if (!file.exists()) {
                return Optional.empty();
            } else {
                File[] listOfFiles = file.listFiles();
                if (listOfFiles != null && listOfFiles.length == 1 && listOfFiles[0].isDirectory()) {
                    return Optional.ofNullable(PipelineModel.load(listOfFiles[0].getPath()));
                }
            }
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }
}
