package com.example.coursework.filestorage.ipml;

import com.example.coursework.exceptions.NameAlreadyUsedException;
import com.example.coursework.filestorage.UserFileStorage;
import com.example.coursework.models.User;
import com.example.coursework.models.enams.Countries;
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
import static com.example.coursework.helpers.LocalDateGetter.getCurrentDate;
import static com.example.coursework.models.User.getHeaders;

@Component
public class UserFileStorageImpl implements UserFileStorage {
    public static final String DIRNAME = "src/main/resources/users";
    public static final String FILE_PATH = "src/main/resources/users/user-" + getCurrentDate() + ".csv";
    private final AtomicInteger idCounter = new AtomicInteger();
    private final HashMap<Integer, User> hashMap = new HashMap<>();
    private final HashSet<String> uniqueNicknames = new HashSet<>();

    @Override
    @PostConstruct
    public void saveInHashMap() {

        List<User> users = readAllFiles(DIRNAME);

        if (users != null) {
            for (User user : users) {
                uniqueNicknames.add(user.getNickname().toLowerCase());
                hashMap.put(user.getUserId(), user);
                idCounter.set(getLastId(DIRNAME));
            }
        }
    }

    @Override
    public void updateFromFile(final Integer id, final User entity, String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<User> list = fileReader(tempFile);
        list.removeIf((n) -> n.getUserId().equals(id));
        list.add(entity);
        list.sort(Comparator.comparing(User::getUserId));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(getHeaders() + System.lineSeparator());
            for (User user : list) {
                fileWriter.write(user.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromFile(final Integer id, final  String dirname) {
        File tempFile = new File(findFile(id, dirname));
        List<User> list = fileReader(tempFile);
        list.removeIf((n) -> n.getUserId().equals(id));

        try (FileWriter fileWriter = new FileWriter(tempFile, false)) {
            fileWriter.write(getHeaders() + System.lineSeparator());
            for (User user : list) {
                fileWriter.write(user.toCsv() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(final User entity, final String filePath) {
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
    public List<User> fileReader(final File file) {
        List<User> users = new LinkedList<>();
        boolean skipHeaders = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!skipHeaders) {
                    User user = new User();

                    String[] values = line.split(", ");

                    user.setUserId(Integer.parseInt(values[0]));
                    user.setNickname(String.valueOf(values[1]));
                    user.setDateOfRegister(String.valueOf(values[2]));
                    user.setCountry(Countries.valueOf(values[3]));
                    user.setFirstName(String.valueOf(values[4]));
                    user.setLastName(String.valueOf(values[5]));

                    users.add(user);
                } else {
                    skipHeaders = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> readAllFiles(final String dirname) {
        List<User> userList = new ArrayList<>();
        File[] files = getCatalog(dirname);

        assert files != null;
        for (File file : files) {
            if (Files.exists(Path.of(file.toURI()))) {
                File file1 = new File(file.toURI());
                userList.addAll(fileReader(file1));
            }
        }

        return userList;
    }

    @Override
    public Integer incrementIdCounter() {
        return idCounter.incrementAndGet();
    }

    @Override
    public Map<Integer, User> getHashMap() {
        return hashMap;
    }

    @Override
    public List<User> getList() {
        return new ArrayList<>(hashMap.values());
    }

    public void checkNicknames(final Integer id, final User user) {
        if (id != null) {
            User oldUser = hashMap.get(id);
            if (oldUser != null && !oldUser.getNickname().equalsIgnoreCase(user.getNickname())) {
                if (uniqueNicknames.contains(user.getNickname().toLowerCase())) {
                    throw new NameAlreadyUsedException("nickname is already used");
                } else {
                    uniqueNicknames.remove(oldUser.getNickname().toLowerCase());
                    uniqueNicknames.add(user.getNickname().toLowerCase());
                    user.setNickname(user.getNickname());
                }
            }
        } else {
            if (uniqueNicknames.contains(user.getNickname().toLowerCase())) {
                throw new NameAlreadyUsedException("nickname is already used");
            } else {
                uniqueNicknames.add(user.getNickname().toLowerCase());
                user.setNickname(user.getNickname());
            }
        }
    }

    public void deleteFromHashSet(final Integer id) {
        String oldValue = hashMap.get(id).getNickname();
        uniqueNicknames.remove(oldValue.toLowerCase());
    }
}
