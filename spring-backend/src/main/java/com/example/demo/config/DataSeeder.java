package com.example.demo.config;

import com.example.demo.model.Artist;
import com.example.demo.model.Song;
import com.example.demo.repository.ArtistRepository;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Popula o banco de dados com os dados do projeto original (artists.js + songs.js)
 * na primeira execução. Executa apenas se o banco estiver vazio.
 */
@Configuration
public class DataSeeder {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            if (artistRepository.count() > 0) {
                System.out.println("==> Banco já populado. Seed ignorado.");
                return;
            }

            System.out.println("==> Populando banco de dados com dados iniciais...");

            // ── Artistas ────────────────────────────────────────────────────────
            Artist arcticMonkeys = save("Arctic Monkeys",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/f3/ac/06/f3ac06b3-9217-adc8-cc33-8e930293e495/887835044184.png/600x600bb.jpg");
            Artist beatles = save("The Beatles",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/df/db/61/dfdb615d-47f8-06e9-9533-b96daccc029f/18UMGIM31076.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/d1/82/d4/d182d41a-bcbc-fbec-0e67-402efc414b04/26UMGIM82692.rgb.jpg/600x600bb.jpg");
            Artist galinhaPintadinha = save("Galinha Pintadinha",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/f4/a1/fd/f4a1fdaf-24f9-85a3-710a-3c55fb3c5a80/7898614902390.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/46/1e/ec/461eec73-c7c6-afe4-6bba-43a37292e5b6/7898614902420.jpg/600x600bb.jpg");
            Artist radiohead = save("Radiohead",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/07/60/ba/0760ba0f-148c-b18f-d0ff-169ee96f3af5/634904078164.png/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/1b/a9/5c/1ba95cac-b245-d386-63fb-6b857aa9dce8/634904078065.png/600x600bb.jpg");
            Artist pinkFloyd = save("Pink Floyd",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/600x600bb.jpg");
            Artist nirvana = save("Nirvana",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e3/20/03/e32003a4-99bc-1c70-40ba-001882f35dba/00602537526840.rgb.jpg/600x600bb.jpg");
            Artist queen = save("Queen",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e8/f1/51/e8f151ae-0f87-a2fd-b981-807a01b24504/18UMGIM55031.rgb.jpg/600x600bb.jpg");
            Artist rhcp = save("Red Hot Chili Peppers",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/4c/86/1d/4c861dab-5428-f3b7-8068-82bb69db5e89/093624932130.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/5e/49/35/5e493511-d87b-5aa2-b379-30fffbae902b/093624932154.jpg/600x600bb.jpg");
            Artist davidBowie = save("David Bowie",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/5f/fa/56/5ffa56c2-ea1f-7a17-6bad-192ff9b6476d/825646124206.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/bd/df/9d/bddf9d26-d45a-278f-26c1-e5274094cb27/190295671600.jpg/600x600bb.jpg");
            Artist coldplay = save("Coldplay",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/f5/93/8c/f5938c49-964c-31d1-4b33-78b634f71fb7/190295978075.jpg/600x600bb.jpg");
            Artist oasis = save("Oasis",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/a1/2a/6c/a12a6c65-fc8d-a61e-762c-42860d181bd2/884977638448.jpg/600x600bb.jpg");

            // ── Músicas: Arctic Monkeys ──────────────────────────────────────────
            saveSong("Do I Wanna Know?", "4:32", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/b2/df/5c/b2df5c8f-af5d-646a-663c-c15eede6b48e/mzaf_4729988752193461592.plus.aac.p.m4a", arcticMonkeys);
            saveSong("505", "4:13", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/1f/46/84/1f468438-c8ff-6c7c-d790-7d9ad31a55b6/dj.ofwxjvjm.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/66/ff/b1/66ffb1ab-76b5-d3ea-d1ce-506c03dbba9f/mzaf_10396055376076846053.plus.aac.p.m4a", arcticMonkeys);
            saveSong("I Wanna Be Yours", "3:04", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/5f/37/be/5f37be34-5729-45b4-8ed1-5b7bd70b8a68/mzaf_17466306567367397119.plus.aac.p.m4a", arcticMonkeys);
            saveSong("R U Mine?", "3:21", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/b2/77/ac/b277acde-7038-d62e-64bd-f6e35afe798b/mzaf_11778260708492554815.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Fluorescent Adolescent", "2:59", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/1f/46/84/1f468438-c8ff-6c7c-d790-7d9ad31a55b6/dj.ofwxjvjm.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/18/0e/11/180e1132-cdbf-e18b-87dc-b50b8a8e79ed/mzaf_8534528569570955656.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Why'd You Only Call Me When You're High?", "2:41", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/a8/5f/96/a85f962c-88bf-edc5-b841-aea59b6a4a4a/mzaf_1899130315719965244.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Arabella", "3:27", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/69/9c/b5/699cb5d6-115c-ff73-9d26-e57ea4350d72/887828031795.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/47/97/34/479734e2-4b82-2035-d957-04eb8139d39b/mzaf_6557564825528502215.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Crying Lightning", "3:43", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/4a/07/92/4a0792a5-03c9-10d8-a60c-94fa8bb6508a/mzi.nlrajrgr.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/a1/39/c0/a139c0de-722a-5a7a-944b-7e83312ea015/mzaf_11615992920297279403.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Mardy Bum", "2:55", "https://is1-ssl.mzstatic.com/image/thumb/Features125/v4/cf/9b/96/cf9b9637-f619-eceb-5382-e9b4d44e74fb/dj.npwkgmai.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/0a/53/4a/0a534ab0-afbc-15b3-8dfc-087f3991d1d2/mzaf_925220595981036171.plus.aac.p.m4a", arcticMonkeys);
            saveSong("Four out of Five", "5:12", "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/f3/ac/06/f3ac06b3-9217-adc8-cc33-8e930293e495/887835044184.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/fa/73/63/fa7363c0-4d6b-a41b-2cb7-6ecc4ed5c1d5/mzaf_7718326777126411788.plus.aac.p.m4a", arcticMonkeys);

            // ── Músicas: The Beatles ─────────────────────────────────────────────
            saveSong("All You Need Is Love", "3:47", "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/d1/82/d4/d182d41a-bcbc-fbec-0e67-402efc414b04/26UMGIM82692.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/ec/ce/db/eccedb92-c513-d6ac-ced8-529dd0b1a472/mzaf_14820763026913661599.plus.aac.p.m4a", beatles);
            saveSong("Here Comes the Sun", "3:05", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/df/db/61/dfdb615d-47f8-06e9-9533-b96daccc029f/18UMGIM31076.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/e7/bf/a0/e7bfa041-6e35-be4e-276e-df489781b5d4/mzaf_1668350712755343495.plus.aac.p.m4a", beatles);
            saveSong("Yesterday", "2:05", "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/1a/19/db/1a19db26-17ad-b986-11a9-f72ac7a6194b/18UMGIM31214.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/d1/69/2d/d1692d74-fe32-c676-7a1d-00deacae1644/mzaf_11316115358642175957.plus.aac.p.m4a", beatles);
            saveSong("Let It Be", "4:03", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/ae/98/4c/ae984c7a-cd06-a7cd-e8bf-32cb15ba698d/00602567705475.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/0f/f7/e1/0ff7e145-6be6-4341-4fa1-32999d20707f/mzaf_15493778815944217662.plus.aac.p.m4a", beatles);
            saveSong("Blackbird", "2:18", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/fa/5b/89/fa5b898d-bad6-e053-4195-260e5c74f2bb/00602567725466.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview126/v4/81/92/53/81925382-68ea-cee8-2f91-f824ac1dc455/mzaf_8008503031387003585.plus.aac.p.m4a", beatles);

            // ── Músicas: Nirvana ─────────────────────────────────────────────────
            saveSong("Smells Like Teen Spirit", "5:01", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/a6/53/1e/a6531efa-397c-eb73-ecab-9b2790c1471e/mzaf_16440344883389407474.plus.aac.p.m4a", nirvana);
            saveSong("Come As You Are", "3:38", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/f4/3e/86/f43e8664-bdd9-bc56-8d2b-76064c865920/mzaf_15456055651529260945.plus.aac.p.m4a", nirvana);
            saveSong("Lithium", "4:17", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/9e/50/da/9e50da93-eaeb-7f1f-4b7b-2f571ee5ea5e/mzaf_5211214176058454055.plus.aac.p.m4a", nirvana);
            saveSong("Heart-Shaped Box", "4:41", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e3/20/03/e32003a4-99bc-1c70-40ba-001882f35dba/00602537526840.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/c2/e6/8c/c2e68c1c-f722-e98d-1f0e-64ccc75ee60b/mzaf_6980430959019315343.plus.aac.p.m4a", nirvana);
            saveSong("Something In The Way", "3:52", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/95/fd/b9/95fdb9b2-6d2b-92a6-97f2-51c1a6d77f1a/00602527874609.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/cc/b5/cd/ccb5cdf2-26a1-7e4f-779c-98ea3095e1fb/mzaf_11306403304784547761.plus.aac.p.m4a", nirvana);

            // ── Músicas: Queen ───────────────────────────────────────────────────
            saveSong("Bohemian Rhapsody", "5:55", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e8/f1/51/e8f151ae-0f87-a2fd-b981-807a01b24504/18UMGIM55031.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/1d/8a/64/1d8a64be-8dc1-b49c-0bb3-7fbf9e72b6ec/mzaf_13893256498843116547.plus.aac.p.m4a", queen);
            saveSong("Don't Stop Me Now", "3:30", "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/6c/2f/5c/6c2f5c59-82a5-8b5e-ddde-1afe99bce12a/mzaf_12484726432882827729.plus.aac.p.m4a", queen);
            saveSong("We Will Rock You", "2:02", "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/39/40/d2/3940d238-2dd5-8524-2df8-8c57edb89b7b/mzaf_4087001285716524048.plus.aac.p.m4a", queen);
            saveSong("We Are the Champions", "2:59", "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/8b/f1/50/8bf1503a-a4db-4fa6-a3a5-f919509acacd/14UMGIM43392.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/21/fc/48/21fc486d-2a1b-3e44-c20d-25c3d4e67e47/mzaf_7386574887219831640.plus.aac.p.m4a", queen);
            saveSong("Radio Ga Ga", "5:47", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e8/f1/51/e8f151ae-0f87-a2fd-b981-807a01b24504/18UMGIM55031.rgb.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/c6/d7/dc/c6d7dc90-fcfd-1bcf-3e0e-3acbc9c0e1e0/mzaf_8009773085024826621.plus.aac.p.m4a", queen);

            // ── Músicas: Pink Floyd ──────────────────────────────────────────────
            saveSong("Wish You Were Here", "5:38", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/5e/4b/35/5e4b3554-282d-12dd-e420-728287a1d3b1/mzaf_14938061551507100947.plus.aac.p.m4a", pinkFloyd);
            saveSong("Comfortably Numb", "6:22", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/fe/44/11/fe441140-ff52-28ef-d7c4-d87b3084d959/mzaf_14451093892826011979.plus.aac.p.m4a", pinkFloyd);
            saveSong("Another Brick in the Wall, Pt. 2", "3:59", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/2c/ea/73/2cea73d2-b3a6-4a26-05c5-0b17e2f5aebf/mzaf_13625143990048697047.plus.aac.p.m4a", pinkFloyd);
            saveSong("Money", "6:30", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/65/56/9b/65569b50-0b0a-81b6-1c09-b07c9b5f8f86/mzaf_4867291571665882773.plus.aac.p.m4a", pinkFloyd);
            saveSong("Time", "7:06", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/aa/e0/ab/aae0ab6a-d906-a189-81bf-70b56aa43f7a/886445635843.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/12/4e/cb/124ecbce-9095-2b01-c2ee-c4b65a6a0ef0/mzaf_2027178041093891437.plus.aac.p.m4a", pinkFloyd);

            // ── Músicas: Radiohead ───────────────────────────────────────────────
            saveSong("Fake Plastic Trees", "4:50", "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/1b/a9/5c/1ba95cac-b245-d386-63fb-6b857aa9dce8/634904078065.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview126/v4/88/7c/92/887c92d6-0979-9f01-1e9b-6762fca517bd/mzaf_1998812167371306210.plus.aac.p.m4a", radiohead);
            saveSong("Creep", "3:56", "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/1b/a9/5c/1ba95cac-b245-d386-63fb-6b857aa9dce8/634904078065.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/c2/6b/00/c26b00e2-3b89-7e87-2a1e-b8b3cb697432/mzaf_4688421218374993398.plus.aac.p.m4a", radiohead);
            saveSong("Karma Police", "4:22", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/07/60/ba/0760ba0f-148c-b18f-d0ff-169ee96f3af5/634904078164.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/fb/1a/f4/fb1af43d-2a85-fb85-fc39-c16c8c2f87a5/mzaf_16499217474782655041.plus.aac.p.m4a", radiohead);
            saveSong("No Surprises", "3:48", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/07/60/ba/0760ba0f-148c-b18f-d0ff-169ee96f3af5/634904078164.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/25/08/23/250823f1-99ee-f9fd-0eb4-1e70d14fd9d0/mzaf_8476034706671619946.plus.aac.p.m4a", radiohead);
            saveSong("Paranoid Android", "6:23", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/07/60/ba/0760ba0f-148c-b18f-d0ff-169ee96f3af5/634904078164.png/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/80/2a/db/802adb90-46b5-62c2-1783-4d2e31b40619/mzaf_10261748020625447621.plus.aac.p.m4a", radiohead);

            // ── Músicas: Coldplay ────────────────────────────────────────────────
            saveSong("The Scientist", "5:09", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/c6/d9/06/c6d90673-86c4-5d2b-bd8f-e37c9cb2b2d1/mzaf_17629965022022001609.plus.aac.p.m4a", coldplay);
            saveSong("Yellow", "4:29", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/75/b4/b9/75b4b985-d0b7-1e44-8f3b-df3cad5a5ed5/mzaf_16073040268038551618.plus.aac.p.m4a", coldplay);
            saveSong("Fix You", "4:55", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/db/36/53/db3653df-e45f-6a5d-66c0-9bcffebb8e04/mzaf_3791396523505282097.plus.aac.p.m4a", coldplay);
            saveSong("Clocks", "5:07", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/b9/b4/2a/b9b42ad1-1e25-5096-da43-497a247e69a3/190295978051.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/73/c9/ee/73c9ee12-e8a9-c0f2-bb8d-b9efc4cd0eb6/mzaf_7553499696895756988.plus.aac.p.m4a", coldplay);
            saveSong("Viva la Vida", "4:01", "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/f5/93/8c/f5938c49-964c-31d1-4b33-78b634f71fb7/190295978075.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/e7/cb/ad/e7cbad97-2e6e-2e41-d60e-e0e83de3a19c/mzaf_3648819427843487003.plus.aac.p.m4a", coldplay);

            // ── Músicas: Oasis ───────────────────────────────────────────────────
            saveSong("Wonderwall", "4:19", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/e3/09/40/e309408b-71a7-7dfe-6f4d-3c1e3afc0ea0/mzaf_12282083867867285819.plus.aac.p.m4a", oasis);
            saveSong("Don't Look Back in Anger", "4:48", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/f0/eb/e4/f0ebe457-78ef-7fcc-d1dc-1a5c60e9f5f1/mzaf_7817745012095847249.plus.aac.p.m4a", oasis);
            saveSong("Champagne Supernova", "7:27", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/e2/e4/9b/e2e49bdf-c92c-2ff9-c7bd-7e651f2aa6b3/886444642743.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/d0/3a/27/d03a27ba-3b70-5e09-e3a2-1da7f8e9862e/mzaf_13753791063832117085.plus.aac.p.m4a", oasis);
            saveSong("Live Forever", "4:36", "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/a1/2a/6c/a12a6c65-fc8d-a61e-762c-42860d181bd2/884977638448.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/c5/7b/c4/c57bc4eb-6c9c-59f7-38db-86e7a0bcad51/mzaf_14819714461867012571.plus.aac.p.m4a", oasis);
            saveSong("Half the World Away", "4:11", "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/a1/2a/6c/a12a6c65-fc8d-a61e-762c-42860d181bd2/884977638448.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/04/f9/fd/04f9fd2d-9e74-9c73-4aba-04c3e1c84afa/mzaf_6218499093827419219.plus.aac.p.m4a", oasis);

            // ── Músicas: David Bowie ─────────────────────────────────────────────
            saveSong("Heroes", "6:07", "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/5f/fa/56/5ffa56c2-ea1f-7a17-6bad-192ff9b6476d/825646124206.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/3f/00/28/3f002856-d1c7-e2b0-b62d-c54c8f2e2f82/mzaf_8527501501680126099.plus.aac.p.m4a", davidBowie);
            saveSong("Space Oddity", "5:15", "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/5f/fa/56/5ffa56c2-ea1f-7a17-6bad-192ff9b6476d/825646124206.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/c6/44/cb/c644cb99-0cbe-a4c4-d4fe-8de85e5e5e76/mzaf_14481028741048073505.plus.aac.p.m4a", davidBowie);
            saveSong("Starman", "4:10", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/bd/df/9d/bddf9d26-d45a-278f-26c1-e5274094cb27/190295671600.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/6f/42/fc/6f42fcdd-2f9d-8d22-8e3b-42d3bc3fde39/mzaf_6213620374476791225.plus.aac.p.m4a", davidBowie);
            saveSong("Ziggy Stardust", "3:13", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/bd/df/9d/bddf9d26-d45a-278f-26c1-e5274094cb27/190295671600.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/f2/77/24/f27724c5-24a5-a7d9-19b8-d3abd8ab2c0b/mzaf_7474049568498862041.plus.aac.p.m4a", davidBowie);
            saveSong("Let's Dance", "4:07", "https://is1-ssl.mzstatic.com/image/thumb/Music114/v4/5f/fa/56/5ffa56c2-ea1f-7a17-6bad-192ff9b6476d/825646124206.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/8d/8a/e7/8d8ae7f6-14cc-ccef-d69b-d9aa8d00d0de/mzaf_7283905200148126374.plus.aac.p.m4a", davidBowie);

            // ── Músicas: Red Hot Chili Peppers ───────────────────────────────────
            saveSong("Under the Bridge", "4:24", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/4c/86/1d/4c861dab-5428-f3b7-8068-82bb69db5e89/093624932130.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/15/61/af/1561af6a-0d62-a22b-fbf6-f6d57a0dcc3e/mzaf_3419494673697453898.plus.aac.p.m4a", rhcp);
            saveSong("Californication", "5:21", "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/5e/49/35/5e493511-d87b-5aa2-b379-30fffbae902b/093624932154.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/e4/f2/88/e4f2884b-40e7-62b9-d7d7-5124d9fcc4b3/mzaf_9127882984038898702.plus.aac.p.m4a", rhcp);
            saveSong("Scar Tissue", "3:37", "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/5e/49/35/5e493511-d87b-5aa2-b379-30fffbae902b/093624932154.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/12/4b/33/124b33e3-5aab-37cd-dd39-da7c67e2e98c/mzaf_6839133780264052573.plus.aac.p.m4a", rhcp);
            saveSong("By the Way", "3:37", "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/5e/49/35/5e493511-d87b-5aa2-b379-30fffbae902b/093624932154.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/99/51/4d/99514d21-4770-dde4-16fe-45cd29f46f82/mzaf_5779012832684049786.plus.aac.p.m4a", rhcp);
            saveSong("Dani California", "4:42", "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/5e/49/35/5e493511-d87b-5aa2-b379-30fffbae902b/093624932154.jpg/300x300bb.jpg", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/24/13/76/2413763b-2bda-e51d-0e1c-7f2bce1b3571/mzaf_13479668540268960451.plus.aac.p.m4a", rhcp);

            // ── Músicas: Galinha Pintadinha ──────────────────────────────────────
            saveSong("Galinha Pintadinha", "1:47", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/f4/a1/fd/f4a1fdaf-24f9-85a3-710a-3c55fb3c5a80/7898614902390.jpg/300x300bb.jpg", "placeholder", galinhaPintadinha);
            saveSong("Pintinho Amarelinho", "1:43", "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/f4/a1/fd/f4a1fdaf-24f9-85a3-710a-3c55fb3c5a80/7898614902390.jpg/300x300bb.jpg", "placeholder", galinhaPintadinha);
            saveSong("Borboletinha", "1:44", "https://is1-ssl.mzstatic.com/image/thumb/Music5/v4/ac/d6/0d/acd60d1c-1d99-bf0b-95b5-1407cea8dab4/7898614902406.jpg/300x300bb.jpg", "placeholder", galinhaPintadinha);

            // ── Led Zeppelin ─────────────────────────────────────────────────────
            Artist ledZeppelin = save("Led Zeppelin",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Features125/v4/59/b1/8d/59b18dc8-eafc-b4b3-9e29-c97fb51aadce/dj.oxvutfbf.jpg/600x600bb.jpg");
            saveSong("Stairway to Heaven", "8:02", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/300x300bb.jpg", "placeholder", ledZeppelin);
            saveSong("Whole Lotta Love", "5:34", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/300x300bb.jpg", "placeholder", ledZeppelin);
            saveSong("Kashmir", "8:32", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/300x300bb.jpg", "placeholder", ledZeppelin);
            saveSong("Black Dog", "4:55", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/300x300bb.jpg", "placeholder", ledZeppelin);
            saveSong("Rock and Roll", "3:40", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3f/2e/fb/3f2efb41-4f01-0a09-b25e-68e4d2cdd28f/dj.pjmivxpg.jpg/300x300bb.jpg", "placeholder", ledZeppelin);

            // ── The Rolling Stones ───────────────────────────────────────────────
            Artist rollingstones = save("The Rolling Stones",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/600x600bb.jpg");
            saveSong("Paint It Black", "3:22", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/300x300bb.jpg", "placeholder", rollingstones);
            saveSong("Sympathy for the Devil", "6:18", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/300x300bb.jpg", "placeholder", rollingstones);
            saveSong("Start Me Up", "3:33", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/300x300bb.jpg", "placeholder", rollingstones);
            saveSong("(I Can't Get No) Satisfaction", "3:44", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/300x300bb.jpg", "placeholder", rollingstones);
            saveSong("Gimme Shelter", "4:31", "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/e6/d5/99/e6d59984-84b5-5ba5-59dd-61ec3adceea8/18UMGIM11555.rgb.jpg/300x300bb.jpg", "placeholder", rollingstones);

            // ── The Doors ────────────────────────────────────────────────────────
            Artist doors = save("The Doors",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/600x600bb.jpg");
            saveSong("Light My Fire", "7:06", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/300x300bb.jpg", "placeholder", doors);
            saveSong("Riders on the Storm", "7:14", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/300x300bb.jpg", "placeholder", doors);
            saveSong("Break On Through", "2:29", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/300x300bb.jpg", "placeholder", doors);
            saveSong("People Are Strange", "2:12", "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/b5/20/43/b520430c-e4b2-d8ed-fef8-3c3d41dc46dd/dj.fbbuzovv.jpg/300x300bb.jpg", "placeholder", doors);

            // ── Fleetwood Mac ────────────────────────────────────────────────────
            Artist fleetwoodMac = save("Fleetwood Mac",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/600x600bb.jpg");
            saveSong("Dreams", "4:14", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/300x300bb.jpg", "placeholder", fleetwoodMac);
            saveSong("Go Your Own Way", "3:38", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/300x300bb.jpg", "placeholder", fleetwoodMac);
            saveSong("The Chain", "4:30", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/300x300bb.jpg", "placeholder", fleetwoodMac);
            saveSong("Everywhere", "3:36", "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/d5/14/c4/d514c4ce-8dde-8e32-fc3a-9b1c76e0b0dc/00603497855230.rgb.jpg/300x300bb.jpg", "placeholder", fleetwoodMac);

            // ── Aerosmith ────────────────────────────────────────────────────────
            Artist aerosmith = save("Aerosmith",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/600x600bb.jpg");
            saveSong("Dream On", "4:26", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/300x300bb.jpg", "placeholder", aerosmith);
            saveSong("Sweet Emotion", "4:35", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/300x300bb.jpg", "placeholder", aerosmith);
            saveSong("Walk This Way", "3:41", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/300x300bb.jpg", "placeholder", aerosmith);
            saveSong("I Don't Want to Miss a Thing", "4:58", "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/0c/c2/b0/0cc2b0d5-0e5a-1a91-17d5-0fe96e71b1e7/00731454027427.rgb.jpg/300x300bb.jpg", "placeholder", aerosmith);

            // ── Black Sabbath ────────────────────────────────────────────────────
            Artist blackSabbath = save("Black Sabbath",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/600x600bb.jpg");
            saveSong("Paranoid", "2:48", "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/300x300bb.jpg", "placeholder", blackSabbath);
            saveSong("Iron Man", "5:58", "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/300x300bb.jpg", "placeholder", blackSabbath);
            saveSong("War Pigs", "7:57", "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/300x300bb.jpg", "placeholder", blackSabbath);
            saveSong("Sabbath Bloody Sabbath", "5:44", "https://is1-ssl.mzstatic.com/image/thumb/Music123/v4/06/8b/60/068b607e-1e98-e2fd-5714-ca9f7f897b4f/13UABIM27397.rgb.jpg/300x300bb.jpg", "placeholder", blackSabbath);

            // ── AC/DC ────────────────────────────────────────────────────────────
            Artist acdc = save("AC/DC",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/600x600bb.jpg",
                    "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/600x600bb.jpg");
            saveSong("Back in Black", "4:15", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/300x300bb.jpg", "placeholder", acdc);
            saveSong("Highway to Hell", "3:28", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/300x300bb.jpg", "placeholder", acdc);
            saveSong("Thunderstruck", "4:52", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/300x300bb.jpg", "placeholder", acdc);
            saveSong("TNT", "3:35", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/300x300bb.jpg", "placeholder", acdc);
            saveSong("You Shook Me All Night Long", "3:30", "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/4d/a4/e8/4da4e820-9e56-39f5-8cbc-7d3706e0f3f8/dj.ngzxwxdf.jpg/300x300bb.jpg", "placeholder", acdc);

            System.out.println("==> Seed concluído! " + songRepository.count() + " músicas e " + artistRepository.count() + " artistas cadastrados.");
        };
    }

    private Artist save(String name, String imageUrl, String bannerUrl) {
        return artistRepository.save(Artist.builder()
                .name(name)
                .imageUrl(imageUrl)
                .bannerUrl(bannerUrl)
                .build());
    }

    private void saveSong(String name, String duration, String imageUrl, String audioUrl, Artist artist) {
        songRepository.save(Song.builder()
                .name(name)
                .duration(duration)
                .imageUrl(imageUrl)
                .audioUrl(audioUrl)
                .artist(artist)
                .build());
    }
}
