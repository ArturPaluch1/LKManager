package LKManager.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Element;

@Controller
public class IndexController {


    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"", "/", "index.html"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException, InterruptedException, URISyntaxException {

        Document doc = Jsoup.connect("http://www.managerzone.com.pl/viewtopic.php?t=20727").get();


        String title = doc.title();
        Elements doc1 = doc.getElementsByClass("postbody");
        //doc1= doc1.stream().forEach();


        int i = 0;
        int licznikDruzynTM = 0;
//int runda =0;

        //getElementsByTag("span");
        String string = "";

        Element mecze;

     /*   for (Element element:doc1.get(0).child(1).children()
             ) {
            if(element.tagName().equals("span")&& element .text().toLowerCase().contains("runda")) {
                System.out.println(element.text());

            }
        }*/
        int ii;


        for (Element element : doc1.get(0).child(1).getAllElements().select("span")
        ) {
            if (element.text().toLowerCase().contains("runda")) {
                ii = element.siblingIndex();


                //              System.out.println(element.text());

            }
        }


        List<String> matches = new ArrayList<>();


        String runda = "";
        String teams = "";
        List<Node> links1 = doc.childNodes();
        int index = 0;
        Elements links = doc1.get(0).getAllElements();
        Element rzeszow = (Element) ((Element) links1.get(2)).getElementsByClass("postbody").get(0).childNode(12);
        for (Node node : rzeszow.childNodes()
        ) {

            if (node.nodeName().equals("span") && node.firstChild().toString().toLowerCase().contains("rzeszów") && node.siblingIndex() < 85) {
                index = node.siblingIndex();

                teams = ((Element) node).text();
                System.out.println(rzeszow.childNode(index + 1).firstChild() + "\n");
                System.out.println(rzeszow.childNode(index + 2).firstChild() + "\n");
                System.out.println(rzeszow.childNode(index + 3).firstChild() + "\n");
                String s = node.nextSibling().nodeName();
                int p = 0;


            } else if (node.nodeName().equals("span") && ((Element) node).children().text().toLowerCase().contains("runda")) {
                runda = ((Element) node).text();
            } else if (index != 0) {
                if (node.toString().contains("yarek") || node.toString().contains("kingsajz") || node.toString().contains("marc") || node.toString().contains("jerzykw")) {
                    matches.add(((TextNode) node).text());
                } else if (node.nextSibling().toString().toLowerCase().contains("runda")) {
                    break;
                }
            }


        }


        //      System.out.println(links.get(31).text());
        int indexRzeszowa = 0;
        for (Element element : links
        ) {


            if (element.text().toLowerCase().contains("rzeszów") && links.indexOf(element) > 31) {
                indexRzeszowa = links.indexOf(element);
                //           System.out.println("------------------\n" + element.text());


                mecze = element;
            } else if (links.indexOf(element) > indexRzeszowa && links.indexOf(element) < (indexRzeszowa + 4) && indexRzeszowa != 0) {
                //      System.out.println(element.text() + "\n");
            }
        }

        //    System.out.println(doc1.get(0)+ " \n");

  /*    links = (Element) doc1.get(0).childNode(3).childNode(1);
        for (Node node: links.childNodes()
             ) {
            
        }*/
    /*    for (Element link : links.getElementsByTag("span")) {

            if (link.text().toLowerCase().contains("runda")) {
               // runda++;
                if(i==0)
                {
                    System.out.println(link.text() + " \n");
                    i++;
                }


            }*/

        /*
if(i==1)
{

        if(link.text().contains("Rzeszów"))
        {
            System.out.println(link.text() + " \n");
            int druzyny=0;
            for (Element br:link.nextElementSiblings()
            ) {
                if(br.tag().equals("<br>") && druzyny<4)
                {
                    System.out.println(br.text() + " \n");
                    druzyny++;
                }
            }

        }


}

        }
*/
        Node node = fun();

        //  fun
        //////////////////////////////////////////////////////////
//---------------------------------------------------------------------------------

// doc1 = nody postow
        int po = 0;
        for (Node n1 : doc1
        ) {
            System.out.println("+++++++++++\n" + po);
            po++;
            if (n1.toString().contains("unda")) {
                for (Node n2 : n1.childNodes()
                ) {
                    if (n2.toString().contains("unda")) {
                        for (Node n3 : n2.childNodes()) {
                            if (!n3.toString().equals(" ") && !n3.toString().equals("<br>")) {
                                if (readNode(n3).contains("unda")) {
                                    if (sprawdzDate(n3)) {
                                        System.out.println("----------------\n" + n3.toString());
                                        int n3i = ((Element) n3).elementSiblingIndex();
                                        for (Node n4 : n2.childNodes()
                                        ) {
                                            if (!n3.toString().equals(" ") && !n3.toString().equals("<br>")) {
                                                if (n4.getClass().equals(Element.class)) {
                                                    if (((Element) n4).elementSiblingIndex() > n3i && !n4.toString().equals("<br>")) {

                                                        System.out.println("\n---" + readNode(n4));
                                                    }
                                                } else if (n4.getClass().equals(TextNode.class)) {
                                                    if (!(((TextNode) n4).text().equals(" ") || ((TextNode) n4).text().equals("<br>") || ((TextNode) n4).text().equals("\n"))) {
                                                        if (((Element) n4.previousSibling()).elementSiblingIndex() > n3i) {
                                                            System.out.println("---" + readNode(n4));
                                                        }
                                                    }

                                                }

                                            }
                                        }
                                    }






   /*    if(readNode(n3).contains("ódź"))
       {
           for (Node n4: n3.siblingNodes()
                ) {
              if(((Element) n4).elementSiblingIndex()>((Element) n3).elementSiblingIndex())
              {
                  System.out.println("\n---"+n4.toString());
              }
               System.out.println("\n---"+n4.toString());
           }
       }*/
                                    //  System.out.println("----------------\n"+n3.toString());
                                }


                            }

                        }

                    }

                }

            }

        }


//----------------------------------------------------------------------
//**********************************************************************************************************
        Document document = Jsoup.connect("http://www.managerzone.com.pl/viewtopic.php?t=20727").get();
        Elements doc2 = document.getElementsByClass("postbody");

        //   Element upcomingMatches = (Element) ((Element) doc1.get(0).getAllElements();.get(2)).getElementsByClass("postbody").get(0).childNode(12);
        int yyy = -1;
        for (Node tempNode : doc2) {
            yyy++;
            if (tempNode.toString().contains("ódź") && tempNode.toString().contains("unda")) {

//Wyciąganie daty z Stringa - posta
                Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d{4}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(((Element) tempNode).text());
                //    boolean matchFound = matcher.find();
                int start = -1;
                int end = -1;

                while (matcher.find()) {
                    start = matcher.start();
                    end = matcher.end();

                    if (start != -1) {
                        String dataRundy = ((Element) tempNode).text().substring(start, end);

                        //parsowanie stringa formatu z forum na date
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate dataZeStringa = LocalDate.parse(dataRundy, formatter);

//ile dni minęło od ostatniego wtorku
                        byte dniOdWtorku = 99;
                        switch (LocalDate.now().getDayOfWeek()) {
                            case MONDAY: {
                                dniOdWtorku = 6;
                                break;
                            }
                            case TUESDAY: {
                                dniOdWtorku = 7;
                                break;
                            }
                            case WEDNESDAY: {
                                dniOdWtorku = 1;
                                break;
                            }
                            case THURSDAY: {
                                dniOdWtorku = 2;
                                break;
                            }
                            case FRIDAY: {
                                dniOdWtorku = 3;
                                break;
                            }
                            case SATURDAY: {
                                dniOdWtorku = 4;
                                break;
                            }
                            case SUNDAY: {
                                dniOdWtorku = 5;
                                break;
                            }
                            default:
                                break;
                        }


//Data w poście jest późniejsza niż ostatni wtorek
                        if (dataZeStringa.isAfter(LocalDate.now().minusDays(dniOdWtorku))) {
                            //czy kolejny nore (nie br/ " " ma text rzeszów
                            int y = 1;
                            for (Node nodeInPost : tempNode.childNodes()
                            ) {
                                boolean containsLiga = false;
                                boolean containsRunda = false;
                                try {
                                    containsLiga = ((Element) nodeInPost).text().toLowerCase().contains("liga");
                                    containsRunda = ((Element) nodeInPost).text().toLowerCase().contains("runda");
                                } catch (Exception e) {
                                    containsLiga = ((TextNode) nodeInPost).text().toLowerCase().contains("liga");
                                    containsRunda = ((TextNode) nodeInPost).text().toLowerCase().contains("runda");
                                }

                                if (containsLiga) {
                                    rec(nodeInPost);

               /*                if(((TextNode) nodeInPost).text().toLowerCase().contains("br")||((TextNode) nodeInPost).text().toLowerCase().contains("br"))
                               {

                               }
                               else if(((TextNode) nodeInPost).text().toLowerCase().contains(""))
                               {

                               }

*/

                                } else if (containsRunda) {
                                    rec(nodeInPost);
                                }

                            }


                            if (y == 0) {
                                int yy = 0;
                            }

                        } else if (!LocalDate.now().minusDays(dniOdWtorku).isAfter(dataZeStringa)) {


                        }
                        //       ((Element) node).text().matches(String.valueOf(p));
                    }


                }

              /*  if (matchFound) {
                start= matcher.start();
                    end = matcher.end();
                }*/


            }
            int o = 0;
        }


//***********************************************************************************


        Elements e = doc.getElementsByTag("span");
        model.addAttribute("matches", matches);
        model.addAttribute("teams", teams);
        model.addAttribute("runda", runda);
        //   model.addAttribute("oo", node);
        return "index";
    }

    private boolean sprawdzDate(Node tempNode) {

        Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(((Element) tempNode).text());
        //    boolean matchFound = matcher.find();
        int start = -1;
        int end = -1;

        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();

            if (start != -1) {
                String dataRundy = ((Element) tempNode).text().substring(start, end);

                //parsowanie stringa formatu z forum na date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate dataZeStringa = LocalDate.parse(dataRundy, formatter);

//ile dni minęło od ostatniego wtorku
                byte dniOdWtorku = 99;
                switch (LocalDate.now().getDayOfWeek()) {
                    case MONDAY: {
                        dniOdWtorku = 6;
                        break;
                    }
                    case TUESDAY: {
                        dniOdWtorku = 7;
                        break;
                    }
                    case WEDNESDAY: {
                        dniOdWtorku = 1;
                        break;
                    }
                    case THURSDAY: {
                        dniOdWtorku = 2;
                        break;
                    }
                    case FRIDAY: {
                        dniOdWtorku = 3;
                        break;
                    }
                    case SATURDAY: {
                        dniOdWtorku = 4;
                        break;
                    }
                    case SUNDAY: {
                        dniOdWtorku = 5;
                        break;
                    }
                    default:
                        break;
                }


//Data w poście jest późniejsza niż ostatni wtorek
                if (dataZeStringa.isAfter(LocalDate.now().minusDays(dniOdWtorku))) {
                    return true;
                }
            }
        }
        return false;
    }


    //**************************************************
    ///////////////////////////////////////////////////////////////
    private void rec(Node nodeInPost) {


        nodeInPost = skipEmpties(nodeInPost);
        if (nodeInPost != null) {
            // if (((TextNode) nodeInPost).text().contains("ódź")) {
            if (readNode(nodeInPost).contains("ódź")) {
                System.out.println("wszedl w lodz");
                //   rec(nodeInPost.nextSibling());
                //     System.out.println(readNode(nodeInPost));
                nodeInPost = skipEmpties(nodeInPost);
                if (nodeInPost != null) {

                    // taki przypadek
                    if (readNode(nodeInPost).contains("-")) {
                        System.out.println("wszedl w :");
                        System.out.println(readNode(nodeInPost));

                        while (nodeInPost != null) {
                            nodeInPost = nodeInPost.nextSibling();
                            nodeInPost = skipEmpties(nodeInPost);
                            if (nodeInPost != null)
                                System.out.println(readNode(nodeInPost));

                        }
                    }


                }


            } else {

            }


        }


        /////////////////////////////
    }

    /*****************************/

    private String readNode(Node node) {
        if (node.getClass().equals(org.jsoup.nodes.Element.class)) {
            return ((Element) node).text();
        } else {
            return ((TextNode) node).text();
        }
    }


    /* ************************ */

    private Node skipEmpties(Node node) {
        if (node != null) {
            if (node.nextSibling() != null) {
                if (node.nextSibling().getClass().equals(org.jsoup.nodes.Element.class)) {
                    if (((Element) node.nextSibling()).text().equals(" ") || ((Element) node.nextSibling()).text().equals("")) {
                        skipEmpties(node.nextSibling());
                    } else if (((Element) node.nextSibling()).text().equals("br")) {
                        node = node.nextSibling();
                    }


                } else if (((TextNode) node.nextSibling()).text().equals(" ") || ((TextNode) node.nextSibling()).text().equals("")) {

                    skipEmpties(node.nextSibling());
                } else {

                    node = node.nextSibling();

                }

            }

        }
        return node;
    }


    Node fun() throws IOException {


        return null;
    }

    private Node szukanieWposcie(Node node1) {
        String runda = "";
        String teams = "";
        List<String> matches = new ArrayList<>();

        int i = 0;
        for (Node node : node1.childNodes()
        ) {
            if (!node.nodeName().equals("br")) {

                Pattern pattern = Pattern.compile("\\(\\w*\\)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(((TextNode) node).text());
                boolean matchFound = matcher.find();

                if (matchFound) {
                    matches.add(((TextNode) node).text());
                } else if (((TextNode) node).text().contains("unda")) {
                    runda = ((TextNode) node).text();
                } else if (((TextNode) node).text().contains("ódź")) {


                    teams = ((TextNode) node).text();

                }

            }


        }


         /*   if (node.nodeName().equals("span") && node.firstChild().toString().toLowerCase().contains("rzeszów") && node.siblingIndex() < 85) {
                index = node.siblingIndex();

                teams = ((Element) node).text();
                System.out.println(rzeszow.childNode(index + 1).firstChild() + "\n");
                System.out.println(rzeszow.childNode(index + 2).firstChild() + "\n");
                System.out.println(rzeszow.childNode(index + 3).firstChild() + "\n");
                String s = node.nextSibling().nodeName();
                int p = 0;


            } else*/


        return node1;


    }
}

