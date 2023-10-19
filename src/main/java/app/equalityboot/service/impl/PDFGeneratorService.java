package app.equalityboot.service.impl;

import app.equalityboot.model.UserDetails;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFGeneratorService {
    public void export(HttpServletResponse response, UserDetails userDetails) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.open();
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-8");
        Font fontTitle = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontTitle.setSize(14);
        Font fontParagraph = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontParagraph.setSize(10);
        Font fontForAddition = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontForAddition.setSize(8);

        Paragraph titleForFirstPage = new Paragraph("KWESTIONARIUSZ OSOBOWY DLA OSOBY" + "\n" +" UBIEGAJACEJ SIE O ZATRUDNIENIE", fontTitle);
        titleForFirstPage.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph firstParagraph = new Paragraph(getFirstPage(userDetails), fontParagraph);
        firstParagraph.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(titleForFirstPage);
        document.add(firstParagraph);
        document.newPage();

        Paragraph titleForSecondPage = new Paragraph("DODATKOWE INFORMACJE DLA OSOBY" + "\n" + "UBIEGAJĄCEJ SIĘ O ZATRUDNIENIE", fontTitle);
        titleForSecondPage.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph secondParagraph = new Paragraph(getSecondPage(), fontParagraph);
        secondParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph additionForSecondPage = new Paragraph(getAdditionForSecondPage(), fontForAddition);
        additionForSecondPage.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(titleForSecondPage);
        document.add(secondParagraph);
        document.add(additionForSecondPage);
        document.newPage();

        Paragraph titleForThirdPage = new Paragraph("SKIEROWANIE NA BADANIA LEKARSKIE", fontTitle);
        titleForThirdPage.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph thirdParagraph = new Paragraph(getThirdPage(userDetails), fontParagraph);
        thirdParagraph.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(titleForThirdPage);
        document.add(thirdParagraph);
        document.newPage();

        Paragraph forthPage = new Paragraph(getFourthPage(), fontParagraph);
        forthPage.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(forthPage);
        document.newPage();

        Paragraph titleForFivePage = new Paragraph("Zgoda na przetwarzanie danych osobowych", fontTitle);
        titleForFivePage.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph fivePage = new Paragraph(getFivePage(),fontParagraph);
        fivePage.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(titleForFivePage);
        document.add(fivePage);
        document.newPage();

        Paragraph titleForSixPage = new Paragraph("KWESTIONARIUSZ OSOBOWY DLA PRACOWNIKA", fontTitle);
        titleForSixPage.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph sixPage = new Paragraph(getSixPage(userDetails), fontParagraph);
        sixPage.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(titleForSixPage);
        document.add(sixPage);

        document.close();
    }

    private String getFirstPage(UserDetails userDetails) {
        String text = "\n \n1. Imię (imiona) i nazwisko   " + userDetails.getUser().getFirstName() + " "+ userDetails.getUser().getLastName()  + "\n" +
                "\n" +
                " 2. Data urodzenia   " + userDetails.getBirthDate() + "\n" +
                "\n" +
                " 3. Dane kontaktowe  " + userDetails.getContactData() + "\n" +
                "\t\t\t                    (wskazane przez osobę ubiegajaca się o zatrudnienie)\n" +
                "\n" +
                " 4. Wykształcenie " + userDetails.getEducation() + "\n" +
                "                  (nazwa szkoły i rok jej ukończenia)" +
                "   ........................................................……………………….............................................................................................\n" +
                "                          (zawód, specjalność, stopień naukowy, tytuł zawodowy, tytuł naukowy)\n" +
                "\n" +
                "5. Kwalifikacje zawodowe .............................................................................." +
                "………………………………………………………………………………………………………………………………………………………………………………………………………………………...\n" +
                "                          (kursy, studia podyplomowe lub inne formy uzupełnienia wiedzy lub umiejętności)\n" +
                "\n" +
                "6. Przebieg dotychczasowego zatrudnienia ...............................................................................................................\n" +
                ".....................................................................................................................................................................................\n" +
                ".....................................................................................................................................................................................\n" +
                "                          (okresy zatrudnienia u kolejnych pracodawców oraz zajmowane stanowiska pracy)\n" +
                "\n" +
                "7. Inne dane osobowe …………………………………………………………………………………………………………….............. " +
                "......................................................................................................................................................\n" +
                "\n" +
                "\n" +
                "Oświadczam, że dane zawarte w kwestionariuszu są zgodne ze stanem prawnym i faktycznym." +
                "\n" +
                "\n" +
                "              ...........................................................                      ......................................................" +
                "\n                             (miejscowość i data)                                 (podpis osoby ubiegającej się o zatrudnienie)                                                              \n \n \n";
        return text;
    }

    private String getSecondPage() {
        String text =
                "\n 1) Administratorem danych osobowych jest " +
                "...........................................................................................................................................................................................\n" +
                "\n" +
                "2) Dane kontaktowe Inspektora Ochrony Danych Osobowych ..................." +
                "....................................................................................................." +
                "..............................................................................................................................................................................................................................................................\n " +
                "\n" +
                "3) Celem przetwarzania danych osobowych jest rekrutacja, a ich nieprzekazanie spowoduje\n" +
                "niemożność uczestnictwa w naborze na stanowisko ........................................................................................................\n" +
                "\n" +
                "4) Dane osobowe będą przechowywane przez administratora przez okres .................................................................. " +
                "..............................................................................................................................................................." +
                "...........................\n" +
                "\n" +
                "5) Osobie ubiegającej się o zatrudnienie przysługuje prawo: dostępu do danych, sprostowania\n" +
                "i usunięcia danych, ograniczenia przetwarzania, przenoszenia danych, wniesienia sprzeciwu wobec\n" +
                "przetwarzania danych, wniesienia skargi do Prezesa Urzędu Ochrony Danych Osobowych\n" +
                "oraz do cofnięcia zgody na wykorzystywanie danych kontaktowych.\n" +
                "\n";
        return text;
    }

    private String getAdditionForSecondPage() {
        return "Podstawa prawna przetwarzania: art. 22' ustawy z dnia 26 czerwca 1974 r. Kodeks pracy\n" +
                "(Dz. U. z 2019 r. poz. 1040 ze zm.), art. 87 ustawy z dnia 20 kwietnia 2004 r. o promocji zatrudnienia i instytucjach\n" +
                "rynku pracy (Dz. U. z 2019 r. poz. 1482 ze zm.), art. 6 ust. 1 lit. b, art. 13 ust. 1 i 2 rozporządzenia Parlamentu\n" +
                "Europejskiego i Rady (UE) 2016/679 z dnia 27 kwietnia 2016 r. w sprawie ochrony osób fizycznych w związku\n" +
                "z przetwarzaniem danych osobowych iw sprawie swobodnego przepływu takich danych oraz uchylenia dyrektywy\n" +
                "95/46/WE (ogólne rozporządzenie o ochronie danych) (Dz. Urz. UE L 119/1 z 4.5.2016).\n" +
                "\n" +
                "1) podaje się jeśli jest to niezbędne do wykonywania pracy określonego rodzaju lub na określonym stanowisku\n" +
                "\n" +
                "2) podaje się, gdy jest to niezbędne do zrealizowania uprawnienia lub spełnienia obowiązku wynikającego z przepisu prawa\n" +
                "3) administrator podaje swoją tożsamość i dane kontaktowe oraz, gdy ma to zastosowanie, tożsamość i dane kontaktowe swojego przedstawiciela\n" +
                "4) wypełnia się, gdy został wyznaczony Inspektor Ochrony Danych Osobowych\n" +
                "\f\n" +
                "              ..........................................................................                    ..............................................................." +
                "\n                             (miejscowość i data)                                              (podpis osoby ubiegającej się o zatrudnienie)                                                              \n \n \n";

    }

    private String getThirdPage(UserDetails userDetails) {
        return "                                         (wstępne/okresowe/kontrolne')\n" +
                "\n" +
                "Działając na podstawie art. 229 § 4a ustawy z dnia 26 czerwca 1974 r.- Kodeks pracy (Dz. U. z 2016 r. poz. 1666) kieruję na badania lekarskie:\n" +
                "\nPana/Panią.        " + userDetails.getUser().getFirstName() + " " +  userDetails.getUser().getLastName() + "\n" +
                "              (imię i nazwisko)\n" +
                "nr PESEL        " + userDetails.getPesel()  + "\n" +
                "zamieszkałego/zamieszkałą        " + userDetails.getLocation() + "\n" +
                "                         (miejscowość, ulica, nr domu, nr lokalu)\n" +
                "\n" +
                "zatrudnionego/zatrudnioną lub podejmującego/podejmującą pracę na stanowisku lub stanowiskach pracy\n" +
                ".............................................................................................................................................................................................\n" +
                ".............................................................................................................................................................................................\n" +
                "określenie stanowiska/stanowisk pracy): .......................................................................................................... \n" +
                ".............................................................................................................................................................................................\n" +
                ".............................................................................................................................................................................................\n" +
                "Opis warunków pracy uwzględniający informacje o występowaniu na stanowisku lub stanowiskach pracy " +
                "czynników niebezpiecznych, szkodliwych dla zdrowia lub czynników uciążliwych iinnych wynikających " +
                "ze sposobu wykonywania pracy, z podaniem wielkości narażenia oraz aktualnych wyników badań i pomiarów " +
                "czynników szkodliwych dla zdrowia, wykonanych na tym stanowisku/stanowiskach — należy wpisać nazwę " +
                "czynnika/czynników i wielkość/wielkości narażenia : \n" +
                "\n" +
                "I Czynniki fizyczne:\n" +
                "\n" +
                "\n" +
                "II. Pyły:\n" +
                "\n" +
                "\n" +
                "III. Czynniki chemiczne:\n" +
                "\n" +
                "\n" +
                "IV. Czynniki biologiczne:\n" +
                "\n" +
                "\n" +
                "V. Inne czynniki, w tym niebezpieczne:\n" +
                "\n" +
                "Łączna liczba czynników niebezpiecznych, szkodliwych dla zdrowia lub czynników uciążliwych i innych\n" +
                "wynikających ze sposobu wykonywania pracy wskazanych w skierowaniu:\n" +
                "\n" +
                "                                                                                                       ..........................................................  " +
                "\n                                                                                                       (podpis pracodawcy)              ";
    }

    private String getFourthPage() {
        return " Objaśnienia:\n" +
                "*)    Niepotrzebne skreślić.\n" +
                "**)   W przypadku osoby, której nie nadano numeru PESEL - seria, numer i nazwa dokumentu potwierdzającego" + "\n"  +
                "      tożsamość, a w przypadku osoby przyjmowanej do pracy - data urodzenia.\n" +
                "***)  Opisać: rodzaj pracy, podstawowe czynności, sposób i czas ich wykonywania.\n" +
                "****) Opis warunków pracy uwzględniający w szczególności przepisy:\n" +
                "        1) wydane na podstawie:\n" +
                "            a) art. 222 § 3 ustawy z dnia 26 czerwca 1974 r. Kodeks pracy dotyczące wykazu substancji - chemicznych, ich mieszanin, czynników lub procesów technologicznych o działaniu rakotwórczym lub mutagennym,\n" +
                "            b) art. 222' § 3 ustawy z dnia 26 czerwca 1974 r. - Kodeks pracy dotyczące wykazu szkodliwych czynników biologicznych,\n" +
                "            c) art. 227 § 2 ustawy z dnia 26 czerwca 1974 r. - Kodeks pracy dotyczące badań i pomiarów\n" +
                "            czynników szkodliwych dla zdrowia,\n" +
                "            d) art. 228 § 3 ustawy z dnia 26 czerwca 1974 r. - Kodeks pracy dotyczące wykazu najwyższych dopuszczalnych stężeń i natężeń czynników szkodliwych dla zdrowia w środowisku pracy,\n" +
                "            e) art. 25 pkt 1 ustawy z dnia 29 listopada 2000 r. Prawo atomowe (Dz. U. z 2014 r. poz. 1512, z późn. zm.) dotyczące dawek granicznych promieniowania jonizującego;\n" +
                "        2) załącznika nr 1 do rozporządzenia Ministra Zdrowia i Opieki Społecznej z dnia 30 maja 1996 r. w sprawie przeprowadzania badań lekarskich pracowników, zakresu profilaktycznej opieki zdrowotnej nad pracownikami oraz orzeczeń lekarskich wydawanych do celów przewidzianych w Kodeksie pracy (Dz. U. z 2016 r. poz. 2067)\n" +
                "Skierowanie na badania lekarskie jest wydawane w dwóch egzemplarzach, z których jeden otrzymuje osoba kierowana na badania.";
    }

    private String getFivePage() {
        return "Ja, niżej podpisana/ny wyrażam zgodę na przetwarzanie moich danych osobowych w podanym wyżej/niżej zakresie\n" +
                ".............................................................................................................................................................................\n" +
                "[zakres przetwarzanych danych powinien być zdefiniowany, jeżeli nie wynika wprost z formularza, pod którym zgoda jest zamieszczona]\n" +
                "\n" +
                "przez......................................................................................................................................................\n" +
                "[nazwa administratora danych i jego adres]\n" +
                "\n" +
                "w celu  ....................................................................................... " +
                "[cel przetwarzania danych (np.\n" +
                "otrzymywanie newslettera z informacjami marketingowymi, przesyłania ofert marketingowych)].\n" +
                "\n" +
                "Informujemy, że Państwa zgoda może zostać cofnięta w dowolnym momencie przez wysłanie wiadomości e-mail na adres naszej firmy spod adresu, którego zgoda dotyczy.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "                                                                                                    ..............................................." +
                "                                                                                                                                 Data, miejsce i podpis osoby wyrażającej zgodę*";
    }

    private String getSixPage(UserDetails userDetails) {
        return " 1. Imię (imiona) i nazwisko  " + userDetails.getUser().getFirstName() + " " + userDetails.getUser().getLastName() + "\n" +
                "2. Numer ewidencyjny PESEL  " + userDetails.getPesel() +  "\n" +
                "3. Numer identyfikacji podatkowej (NIP)  ......................................................................\n" +
                "\n" +
                "4. Stan rodzinny " + userDetails.getStanRodziny() + "\n" +
                "(imiona i nazwiska oraz daty urodzenia dzieci)\n" +
                "\n" +
                "5. Powszechny obowiązek obrony:\n" +
                "\n" +
                "a) stosunek do powszechnego obowiązku obrony ...................................................................\n" +
                "\n" +
                "b) stopień wojskowy ............................................................................................\n" +
                "\n" +
                "numer specjalności wojskowej ...................................................................................\n" +
                "\n" +
                "c) przynależność ewidencyjna do WKU ............................................................................\n" +
                "\n" +
                "d) numer książeczki wojskowej...................................................................................\n" +
                "\n" +
                "e) przydział mobilizacyjny do sił zbrojnych RP .................................................................\n" +
                "................................................................................................................" +
                "\n" +
                "6. Osoba, którą należy zawiadomić w razie wypadku...............................................................\n" +
                "\n" +
                "                                     (imię i nazwisko, adres, telefon)\n" +
                "\n" +
                "7. Oświadczam, że dane zawarte w pkt 1 i 2 są zgodne z dowodem osobistym seria .............. nr ..........................." +
                "wydanym przez.................................................................................................\n" +
                "\n" +
                "lub innym dowodem tożsamości ............................................................................................................\n" +
                "......................................................................................................................................................................" +
                "\n \n \n \n              ..........................................................................                    ................................................" +
                "\n                             (miejscowość i data)                                              (podpis osoby ubiegającej się o zatrudnienie)                                                              \n \n \n";

    }
}
