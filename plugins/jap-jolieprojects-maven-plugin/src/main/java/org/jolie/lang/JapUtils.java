package org.jolie.lang;

import org.codehaus.plexus.util.StringInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JapUtils {

    private List<String> fileList;
    private File outputJapFile;
    private final String projectName;
    private final String projectGroupId;
    private final String projectVersion;

    public JapUtils( File outputJapFile, String projectGroupId, String projectName, String projectVersion ) {
        fileList = new ArrayList< String >();
        this.outputJapFile = outputJapFile;
        this.projectGroupId = projectGroupId;
        this.projectName = projectName;
        this.projectVersion = projectVersion;
    }

    public void createJap(File folderToCompress) {
        byte[] buffer = new byte[1024];
        generateFileList(folderToCompress, folderToCompress);
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(outputJapFile);
            zos = new ZipOutputStream(fos);
            FileInputStream in = null;

            for (String file: this.fileList) {
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                in = new FileInputStream( folderToCompress.toString() +  File.separator + file);
                writingEntry(in, buffer, zos, ze );
            }

            // creating META-INF
            ZipEntry ze = new ZipEntry( "META-INF" + File.separator + "MANIFEST.MF" );
            zos.putNextEntry(ze);
            InputStream stin = new ByteArrayInputStream("test".getBytes() );
            writingEntry(stin, buffer, zos, ze );

            // creating maven
            ze = new ZipEntry( "META-INF" + File.separator + "maven" + File.separator
                    + projectGroupId + File.separator + projectName + File.separator + "pom.properties" );
            zos.putNextEntry(ze);
            String content = "version="+projectVersion+"\ngroupId="+projectGroupId+"\nartifactId="+projectName;
            stin = new ByteArrayInputStream( content.getBytes() );
            writingEntry(stin, buffer, zos, ze );

            // adding pom

            
            zos.closeEntry();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node, File sourceFolder) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsolutePath(), sourceFolder));
        }

        if (node.isDirectory()) {
            if ( !node.getName().equals("lib") ) {
                // skip folder lib
                String[] subNode = node.list();
                for (String filename : subNode) {
                    generateFileList(new File(node, filename), sourceFolder);
                }
            }
        }
    }

    private String generateZipEntry(String file, File sourceFolder) {
        return file.substring(sourceFolder.toString().length() + 1);
    }

    private void writingEntry( InputStream in, byte[] buffer, ZipOutputStream zos, ZipEntry ze ) throws IOException {
        try {
            int len;
            while ((len = in .read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            System.out.println("Added entry " + ze.toString() );
        } finally {
            if ( in != null ) {
                in.close();
            }
        }
    }
}
