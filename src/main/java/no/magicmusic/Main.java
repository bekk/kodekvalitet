package no.magicmusic;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {

    ////
    // Not working? Log in at https://docs.genius.com/#/getting-started-h1
    // (click autorize)
    // and get your own auth thingy there...
    // //
    public static final String T = "RonXd8_fl4L5RZ2CuCOoDHOGlZ-l_i6u0ytzJPDmiPXVihLWdw0cEY4Mj1u8NRaj";

    static Map<String, SongObject> CACHE_CONTAINER;

    public static void main(String[] args) {
        CloseableHttpResponse resp;
        CloseableHttpResponse resp2;
        Brand.ing();
        System.out.println();
        System.out.println("\nSearch:");
        String exit = "";
        while (!exit.equals("exit")) {
            Scanner scnr = new Scanner(System.in);
            String s = scnr.nextLine();
            s = s.replace(" ", "%20");
            if (s.equals("exit")) {
                exit = s;
            } else if (s.isEmpty()) {
                System.out.println("Please write something to search for. Artist/song/whatever!...");
                exit = "bad search";
            } else {
                String uri = "https://api.genius.com/search?q=" + s;
                HttpGet fetch = new HttpGet(uri);
                fetch.addHeader("Authorization", "Bearer " + T);
                CloseableHttpClient cli = HttpClientBuilder.create().build();
                try {
                    resp = cli.execute(fetch);
                    int exception = resp.getStatusLine().getStatusCode();
                    InputStream st = resp.getEntity().getContent();
                    Scanner sc = new Scanner(st).useDelimiter("\\A");
                    String res = sc.hasNext() ? sc.next() : "";
                    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    MusicObject m = mapper.readValue(res, MusicObject.class);
                    if (m != null && m.response != null && m.response.hits != null) {
                        if (!m.response.hits.isEmpty()) {
                            List<Hit> w = new ArrayList<>();
                            for (Hit hit : m.response.hits) {
                                if (hit.result.pyongs_count != null && hit.result.annotation_count != null) {
                                    w.add(hit);
                                }
                            }
                            String plural = w != null ? w.size() > 1 ? "es" : "" : "";
                            System.out.println("Found " + w.size() + " match" + plural + "…");
                            w.sort(new Comparator<Hit>() {
                                @Override
                                public int compare(Hit o1, Hit o2) {
                                    return o2.result.annotation_count.compareTo(o1.result.annotation_count);
                                }
                            });
                           // Hit h = new Hit();
                         //   h.result = w.get(0).result;
                           // if(h.result.pyongs_count == 0) System.err.println("DEBUG: no pyong count. Not expected...");;
                            for (Hit hit : w) {
                                if (hit.result == null || hit.result.primary_artist == null) {
                                    System.out.println("ERROR WITH ARTIST/SONG");
                                } else {
                                    resp2 = null; //todo: stream closing
                                    if(CACHE_CONTAINER == null || CACHE_CONTAINER.isEmpty() || !CACHE_CONTAINER.containsKey(hit.result.id)) {
                                        if(CACHE_CONTAINER == null) {
                                            CACHE_CONTAINER = new HashMap<>();
                                        }
                                        String song = "https://api.genius.com/songs/" + hit.result.id;
                                        HttpGet songfecht = new HttpGet(song);
                                        songfecht.addHeader("Authorization", "Bearer " + T);
                                        resp2 = cli.execute(songfecht);
                                        Scanner sc2 = new Scanner(resp2.getEntity().getContent()).useDelimiter("\\A");
                                        String res2 = sc2.hasNext() ? sc2.next() : "";
                                        //System.out.println(res2);
                                        SongObject a = mapper.readValue(res2, SongObject.class);
                                        CACHE_CONTAINER.put(hit.result.id, a);
                                    }
                                    System.out.println(String.format("%d pyongs: %s with '%s' (released: %s)", hit.result.pyongs_count, hit.result.primary_artist.name, hit.result.title, CACHE_CONTAINER.get(hit.result.id).response.song.release_date));
                                    resp.close();
                                    if(resp2 != null) resp2.close();
                                }
                            }
                        } else {
                            System.out.println("No hits :/");
                        }
                    } else {
                        System.out.println("Something is wrong. Try again…");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\nSearch again:");
            }
        }
        System.out.println("Goodbye! :)");
    }

    static class MusicObject {
        public Response response;
    }

    static class Response {
        public List<Hit> hits;
    }

    static class Hit {
        public Result result;
    }

    static class Result {
        public String id, title;
        public Long pyongs_count, annotation_count;
        public PrimaryArtist primary_artist;
    }

    static class PrimaryArtist {
        public String id;
        public String name;
    }

    static class SongObject {
        public Response2 response;
    }

    static class Response2 {
        public Song song;
    }

    static class Song {
        public String id, name;
        public String release_date;
    }

}