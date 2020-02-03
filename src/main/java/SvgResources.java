import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SvgResources {
    public Set<String> getFilesNamesArray(String pathname) {
        File file = new File(pathname);
        return Stream.of(file.list()).collect(Collectors.toSet());
    }

    public String getFilesNamesJson(String pathname) {
        Gson gson = new Gson();
        return gson.toJson(getFilesNamesArray(pathname));
    }
}
