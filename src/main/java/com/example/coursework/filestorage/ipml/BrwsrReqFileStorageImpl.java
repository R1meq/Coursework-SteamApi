package com.example.coursework.filestorage.ipml;

import com.example.coursework.exceptions.NameAlreadyUsedException;
import com.example.coursework.helpers.LocalDateGetter;
import com.example.coursework.filestorage.BrwsrReqFileStorage;
import com.example.coursework.models.BrwsrReq;
import com.example.coursework.models.enams.SupportedBrwsr;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.coursework.helpers.FileStorageHelper.findFile;
import static com.example.coursework.helpers.FileStorageHelper.getCatalog;
import static com.example.coursework.helpers.FileStorageHelper.getLastId;
import static com.example.coursework.models.BrwsrReq.getHeaders;

@Component
public class BrwsrReqFileStorageImpl implements BrwsrReqFileStorage {
    public static final String DIRNAME = "src/main/resources/brwsrReqs";
    public static final String FILE_PATH = "src/main/resources/brwsrReqs/brwsrReq-" + LocalDateGetter.getCurrentDate() + ".csv";
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Integer, BrwsrReq> hashMap = new HashMap<>();
    private final HashSet<SupportedBrwsr> uniqueNameOfBrwsrReq = new HashSet<>();

    public void checkNameOfBrwsrReq(final BrwsrReq brwsrReq) {
        if (uniqueNameOfBrwsrReq.contains(brwsrReq.getSupportedBrwsr())) {
            throw new NameAlreadyUsedException("name of BrwsrReq already used");
        } else {
            uniqueNameOfBrwsrReq.add(brwsrReq.getSupportedBrwsr());
            brwsrReq.setSupportedBrwsr(brwsrReq.getSupportedBrwsr());
        }
    }

    public void deleteFromHashSet(final Integer id) {
        SupportedBrwsr oldValue = hashMap.get(id).getSupportedBrwsr();
        uniqueNameOfBrwsrReq.remove(oldValue);
    }

    @Override
    public Map<Integer, BrwsrReq> getHashMap() {
        return hashMap;
    }

    @Override
    @PostConstruct
    public void saveInHashMap() {
        List<BrwsrReq> brwsrReqs = readAllFiles(DIRNAME);

        if (brwsrReqs != null) {
            for (BrwsrReq brwsrReq : brwsrReqs) {
                hashMap.put(brwsrReq.getBrwsrReqId(), brwsrReq);
                uniqueNameOfBrwsrReq.add(brwsrReq.getSupportedBrwsr());
                idCounter.set(getLastId(DIRNAME));
            }
        }
    }

    @Override
    public void updateFromFile(final Integer id, final BrwsrReq entity, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<BrwsrReq> list = fileReader(tempFile);
        list.removeIf((n) -> n.getBrwsrReqId().equals(id));
        list.add(entity);
        list.sort(Comparator.comparing(BrwsrReq::getBrwsrReqId));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(getHeaders() + System.lineSeparator());
            for (BrwsrReq brwsrReq : list) {
                fileWriter.write(brwsrReq.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromFile(final Integer id, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<BrwsrReq> list = fileReader(tempFile);
        list.removeIf((n) -> n.getBrwsrReqId().equals(id));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(getHeaders() + System.lineSeparator());
            for (BrwsrReq brwsrReq : list) {
                fileWriter.write(brwsrReq.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(final BrwsrReq entity, final String filePath) {
        File file = new File(filePath);
        boolean fileExists = file.exists();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!fileExists) {
                fileWriter.write(getHeaders() + System.lineSeparator());
            }
            fileWriter.write(entity.toCsv() + System.lineSeparator());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BrwsrReq> fileReader(final File file) {
        List<BrwsrReq> brwsrReqs = new LinkedList<>();
        boolean skipHeaders = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                   BrwsrReq brwsrReq = new BrwsrReq();

                    String[] values = line.split(", ");

                    brwsrReq.setBrwsrReqId(Integer.parseInt(values[0]));
                    brwsrReq.setSupportedBrwsr(SupportedBrwsr.valueOf(values[1]));

                    brwsrReqs.add(brwsrReq);
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return brwsrReqs;
    }

    @Override
    public List<BrwsrReq> readAllFiles(String dirname) {
        List<BrwsrReq> brwsrReqList = new ArrayList<>();
        File[] files = getCatalog(dirname);

        assert files != null;
        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                File file1 = new File(file.toURI());
                brwsrReqList.addAll(fileReader(file1));
            }
        }

        return brwsrReqList;
    }

    @Override
    public Integer incrementIdCounter() {
        return idCounter.incrementAndGet();
    }

    @Override
    public List<BrwsrReq> getList() {
        return new ArrayList<>(hashMap.values());
    }
}
