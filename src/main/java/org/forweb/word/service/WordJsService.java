package org.forweb.word.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.forweb.word.Dump;
import org.forweb.word.view.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.*;

@Service
public class WordJsService {


    public static Dump strategy = Dump.AS_SCRIPT;

    public static String path = "assets/js/word/";

    @Autowired
    WordService wordService;

    @Autowired
    LanguageService languageService;

    @Autowired
    ModuleService moduleService;

    @Autowired
    private ServletContext context;

    public boolean dump() {
        try {
            switch (strategy) {
                case AS_JSON:
                    return dumpWordsAsJson();
                case AS_SCRIPT:
                    return dumpWordsAsScript();
                default:
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getWordsAsJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Dictionary dictionary = new Dictionary(
                languageService.findAll(),
                moduleService.findAll(),
                wordService.findAll()
        );
        return mapper.writeValueAsString(dictionary);
    }

    private boolean dumpWordsAsJson() throws JsonProcessingException {
        return saveFile("word.json", getWordsAsJson());
    }

    private boolean dumpWordsAsScript() throws JsonProcessingException {
        String js = "var Word = {dictionary: JSON.parse("+getWordsAsJson()+")}";
        return saveFile("word.json", js);
    }

    private boolean saveFile(String filename, String content) {
        String servletPath = context.getRealPath("..");
        String dirStr = servletPath + path;
        File dir = new File(dirStr);
        if(!dir.isDirectory() && !dir.mkdirs()) {
            throw new RuntimeException("Can't create folder for js words file");
        }
        try {
            PrintWriter out = new PrintWriter(dirStr + filename);
            out.println(content);
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't create file for js words");
        }
    }
}
