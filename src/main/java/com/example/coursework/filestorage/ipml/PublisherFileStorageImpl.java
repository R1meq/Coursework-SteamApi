package com.example.coursework.filestorage.ipml;

import com.example.coursework.exceptions.NameAlreadyUsedException;
import com.example.coursework.helpers.LocalDateGetter;
import com.example.coursework.filestorage.PublisherFileStorage;
import com.example.coursework.models.Publisher;
import com.example.coursework.models.enams.Publishers;
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


@Component
public class PublisherFileStorageImpl implements PublisherFileStorage {
    public static final String DIRNAME = "src/main/resources/publishers";
    public static final String FILE_PATH = "src/main/resources/publishers/publisher-" + LocalDateGetter.getCurrentDate() + ".csv";
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Integer, Publisher> hashMap = new HashMap<>();
    private final HashSet<Publishers> uniqueNameOfPublisher = new HashSet<>();

    public void checkNameOfPublisher(final Publisher publisher) {
        if (uniqueNameOfPublisher.contains(publisher.getPublisher())) {
            throw new NameAlreadyUsedException("publisher name already used");
        } else {
            uniqueNameOfPublisher.add(publisher.getPublisher());
            publisher.setPublisher(publisher.getPublisher());
        }
    }


    public void deleteFromHashSet(final Integer id) {
        Publishers oldValue = hashMap.get(id).getPublisher();
        uniqueNameOfPublisher.remove(oldValue);
    }

    @Override
    public Map<Integer, Publisher> getHashMap() {
        return hashMap;
    }

    @Override
    @PostConstruct
    public void saveInHashMap() {
        List<Publisher> publishers = readAllFiles(DIRNAME);

        if (publishers != null) {
            for (Publisher publisher : publishers) {
                hashMap.put(publisher.getPublisherId(), publisher);
                uniqueNameOfPublisher.add(publisher.getPublisher());
                idCounter.set(getLastId(DIRNAME));
            }
        }
    }

    @Override
    public void updateFromFile(final Integer id, final Publisher entity, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<Publisher> list = fileReader(tempFile);
        list.removeIf((n) -> n.getPublisherId().equals(id));
        list.add(entity);
        list.sort(Comparator.comparing(Publisher::getPublisherId));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(Publisher.getHeaders() + System.lineSeparator());
            for (Publisher publisher : list) {
                fileWriter.write(publisher.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromFile(final Integer id, final String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<Publisher> list = fileReader(tempFile);
        list.removeIf((n) -> n.getPublisherId().equals(id));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(Publisher.getHeaders() + System.lineSeparator());
            for (Publisher publisher : list) {
                fileWriter.write(publisher.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(final Publisher entity, final String filePath) {
        File file = new File(filePath);
        boolean fileExists = file.exists();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!fileExists) {
                fileWriter.write(Publisher.getHeaders() + System.lineSeparator());
            }
            fileWriter.write(entity.toCsv() + System.lineSeparator());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Publisher> fileReader(final File file) {
        List<Publisher> publishers = new LinkedList<>();
        boolean skipHeaders = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                    Publisher publisher = new Publisher();

                    String[] values = line.split(", ");

                    publisher.setPublisherId(Integer.parseInt(values[0]));
                    publisher.setPublisher(Publishers.valueOf(values[1]));

                    publishers.add(publisher);
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publishers;
    }

    @Override
    public List<Publisher> readAllFiles(String dirname) {
        List<Publisher> publisherList = new ArrayList<>();
        File[] files = getCatalog(dirname);

        assert files != null;
        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                File file1 = new File(file.toURI());
                publisherList.addAll(fileReader(file1));
            }
        }

        return publisherList;
    }

    @Override
    public Integer incrementIdCounter() {
        return idCounter.incrementAndGet();
    }

    @Override
    public List<Publisher> getList() {
        return new ArrayList<>(hashMap.values());
    }

}
