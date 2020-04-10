import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;

public class FileFinder {
    HashMap<IvParameterSpec, Path> fileFound = new HashMap<>();
        
    public void findFiles(String glob, String startLocation) throws IOException {
		
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher(glob);
                
        Files.walkFileTree(Paths.get(startLocation), new SimpleFileVisitor<Path>() {
			
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(path)) {
                    SecureRandom random = new SecureRandom();
                    byte iv[] = new byte[128/8];
                    random.nextBytes(iv);
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    fileFound.put(ivSpec, path);
                }
                                
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

}