import model.Autor;
import model.Comic;
import model.Protagonista;
import services.AutorService;
import services.ComicService;
import services.FileService;
import services.ProtagonistaService;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        ComicService sc = new ComicService();
        AutorService sa = new AutorService();
        ProtagonistaService sp = new ProtagonistaService();

        Autor stan = new Autor("Stan", "Lee", "estadounidense");
        Autor alan = new Autor("Alan", "Moore", "británica");
        Autor frank = new Autor("Frank", "Miller", "estadounidense");
        Autor herge = new Autor("Hergé", "Remi", "belga");

        Protagonista spiderman = new Protagonista("Spider-Man", "superforza e axilidade");
        Protagonista batman = new Protagonista("Batman", "intelixencia e diñeiro");
        Protagonista superman = new Protagonista("Superman", "superforza e voo");
        Protagonista wonderWoman = new Protagonista("Wonder Woman", "forza e velocidade sobrehumanas");
        Protagonista tintin = new Protagonista("Tintín", "intelixencia e capacidade de investigación");
        Protagonista daredevil = new Protagonista("Daredevil", "sentidos sobrehumanos");

        sc.crearComic(new Comic("The Amazing Spider-Man", 1963, stan, spiderman));
        sc.crearComic(new Comic("Batman: The Killing Joke", 1988, alan, batman));
        sc.crearComic(new Comic("Superman: The Man of Steel", 1986, frank, superman));
        sc.crearComic(new Comic("Wonder Woman: Gods and Mortals", 1987, alan, wonderWoman));
        sc.crearComic(new Comic("The Adventures of Tintin", 1929, herge, tintin));
        sc.crearComic(new Comic("Daredevil: Born Again", 1986, stan, daredevil));
        sc.crearComic(new Comic("Batman: Year One", 1987, frank, batman));
        sc.crearComic(new Comic("Spider-Man: Blue", 2002, stan, spiderman));


        System.out.println("LISTADO INICIAL");
        ArrayList<Autor> autores = obterAutores(sc.listarComics());
        ArrayList<Comic> comicsDB = sc.listarComics();

        listarPorAutores(autores,comicsDB);

        //[Recu Ficheiros]
        List<String> linas = FileService.readFicheiro("datos");

        if (linas.size() >= 2) {
            String autorBuscar = linas.get(0); // "Stan Lee"
            String protagonistaBuscar = linas.get(1); // "Spider-Man"

            Long idStan = null;
            for (Comic c : sc.listarComics()) {
                String nomeCompleto = c.getAutor().getNome() + " " + c.getAutor().getApelidos();
                if (nomeCompleto.equalsIgnoreCase(autorBuscar)) {
                    idStan = c.getAutor().getId();
                    break;
                }
            }

            if (idStan != null) {
                sa.actualizarAutor(idStan, "Satan", "Leee");
            }

            Autor autorActualizadoDB = null;
            for (Comic c : sc.listarComics()) {
                if (c.getAutor().getId().equals(idStan)) {
                    autorActualizadoDB = c.getAutor();
                    break;
                }
            }

            if (autorActualizadoDB != null) {
                FileService.serializadorAutor(autorActualizadoDB, "autorActualizado.dat");

                FileService.toXML(autorActualizadoDB, "autorActualizado.xml");
            }

            Long idSpiderman = null;
            for (Comic c : sc.listarComics()) {
                if (c.getProtagonista().getNome().equalsIgnoreCase(protagonistaBuscar)) {
                    idSpiderman = c.getProtagonista().getId();
                    break;
                }
            }

            if (idSpiderman != null) {
                sp.actualizarProtagonista(idSpiderman, "Spooder-Mon");
            }
        } else {
            System.out.println("O ficheiro 'datos' non contén as liñas esperadas.");
        }

        System.out.println("\n Listado Update");
        List<Comic> comicsActualizados = sc.listarComics();
        listarPorAutores(autores,comicsDB);



        Set<Long> idsAutores = new HashSet<>();
        Set<Long> idsProtagonistas = new HashSet<>();

        if (comicsActualizados != null) {
            for (Comic c : comicsActualizados) {
                idsAutores.add(c.getAutor().getId());
                idsProtagonistas.add(c.getProtagonista().getId());
            }

            sc.eliminarTodosOsComics();

            for (Long idAutor : idsAutores) {
                sa.eliminarAutor(idAutor);
            }

            for (Long idProt : idsProtagonistas) {
                sp.eliminarProtagonista(idProt);
            }
        }

        listarPorAutores(autores,comicsDB);
    }

    private static void listarPorAutores(ArrayList<Autor> autores, ArrayList<Comic> comics) {
        if (autores == null || autores.isEmpty()) {
            System.out.println("(Sen datos nas táboas)");
            return;
        }

        for (Autor a : autores) {
            System.out.println("----------------------------------------------");
            System.out.println("Autor:");
            System.out.println(a.getNome() + " " + a.getApelidos());
            System.out.println("Comics:");

            if (comics != null) {
                for (Comic c : comics) {
                    // Comprobamos se o cómic pertence ao autor da iteración actual polo seu ID
                    if (c.getAutor() != null && c.getAutor().getId().equals(a.getId())) {
                        System.out.println(c.getTitulo() + " (" + c.getAno() + ")");
                        System.out.println("\tProtagonista: " + c.getProtagonista().getNome()
                                + " poder: " + c.getProtagonista().getPoder());
                    }
                }
            }
        }
        System.out.println("----------------------------------------------");
    }
    public static ArrayList<Autor> obterAutores(List<Comic> listaComics) {
        ArrayList<Autor> autoresUnicos = new ArrayList<>();

        for (Comic c : listaComics) {
            Autor autor = c.getAutor();
            if (!autoresUnicos.contains(autor)) {
                autoresUnicos.add(autor);
            }
        }

        return autoresUnicos;
    }
}