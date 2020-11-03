package ca.billweb;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HelpLoad {

    static final File jarFile = new File(HelpLoad.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    static void load() throws IOException {
        Variables.helpData = new HashMap<>();
        Variables.categoryHelp = new HashMap<>();
        if(jarFile.isFile()){
            ArrayList<String> files = new ArrayList<>();

            final String path = "resources/help";
            final String fullPath = "ca/billweb/resources/help";
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(fullPath + "/") && !name.equals(fullPath + "/")) { //filter according to the path
                    files.add(name.substring(fullPath.length() + 1));
                }
            }
            jar.close();

            for (String p : files) {
                InputStream stream = HelpLoad.class.getResourceAsStream(path + "/" + p);
                BufferedReader in = new BufferedReader(new InputStreamReader(stream));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }

                JSONObject obj = new JSONObject(sb.toString());
                Variables.helpData.put(obj.getString("name"), obj);

                if (!Variables.categoryHelp.containsKey(obj.getString("category")))
                    Variables.categoryHelp.put(obj.getString("category"), new ArrayList<>());

                Variables.categoryHelp.get(obj.getString("category")).add(obj);
            }
        }
        else {
            File helpDir = new File(HelpLoad.class.getResource("resources/help").getFile());

            System.out.println(Arrays.toString(helpDir.listFiles()));

            for (File f : helpDir.listFiles()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }

                JSONObject obj = new JSONObject(sb.toString());
                Variables.helpData.put(obj.getString("name"), obj);

                if (!Variables.categoryHelp.containsKey(obj.getString("category")))
                    Variables.categoryHelp.put(obj.getString("category"), new ArrayList<>());

                Variables.categoryHelp.get(obj.getString("category")).add(obj);
            }
        }
    }
}
