package services;

import model.Autor;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public static List<String> readFicheiro(String rutaFicheiro) {
        List<String> lista = new ArrayList<>();
        File file = new File(rutaFicheiro);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lista.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("Erro lendo o ficheiro: " + e.getMessage());
            }
        } else {
            System.out.println("Non existe o ficheiro: " + rutaFicheiro);
        }
        return lista;
    }

    public static void toXML(Autor autor, String ruta) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try {
            FileWriter fw = new FileWriter(ruta);
            XMLStreamWriter xsw = xof.createXMLStreamWriter(fw);
            xsw.writeStartDocument("UTF-8", "1.0");
            xsw.writeCharacters("\n");

            xsw.writeStartElement("autor");
            xsw.writeAttribute("id", String.valueOf(autor.getId()));
            xsw.writeCharacters("\n");

            xsw.writeCharacters("  ");
            xsw.writeStartElement("nome");
            xsw.writeCharacters(autor.getNome() != null ? autor.getNome() : "");
            xsw.writeEndElement();
            xsw.writeCharacters("\n");

            xsw.writeCharacters("  ");
            xsw.writeStartElement("apelidos");
            xsw.writeCharacters(autor.getApelidos() != null ? autor.getApelidos() : "");
            xsw.writeEndElement();
            xsw.writeCharacters("\n");

            xsw.writeCharacters("  ");
            xsw.writeStartElement("nacionalidade");
            xsw.writeCharacters(autor.getNacionalidade() != null ? autor.getNacionalidade() : "");
            xsw.writeEndElement();
            xsw.writeCharacters("\n");

            xsw.writeEndElement();
            xsw.writeCharacters("\n");
            xsw.writeEndDocument();

            xsw.flush();
            xsw.close();
            fw.close();
            System.out.println("Arquivo XML xerado correctamente en: " + ruta);

        } catch (Exception e) {
            System.err.println("Erro ao xerar XML: " + e.getMessage());
        }
    }

    public static void serializadorAutor(Autor autor, String ruta) {
        File file = new File(ruta);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(autor);
            System.out.println("Autor serializado correctamente en: " + ruta);

        } catch (Exception e) {
            System.err.println("Erro durante a serialización: " + e.getMessage());
        }
    }

    public static Autor deserializadorAutor(String ruta) {
        File file = new File(ruta);
        Autor autor = null;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            autor = (Autor) ois.readObject();
            System.out.println("Autor deserializado correctamente desde: " + ruta);

        } catch (Exception e) {
            System.err.println("Erro durante a deserialización: " + e.getMessage());
        }

        return autor;
    }
}