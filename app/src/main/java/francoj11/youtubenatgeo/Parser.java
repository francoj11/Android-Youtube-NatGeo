package francoj11.youtubenatgeo;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;



/**
 * Parser for Youtube XML feeds.
 */
public class Parser {
	private SAXParser sp;
	private XMLReader xr;
	private List<VideoInfo> playlist;

    /**
     * Constructor
     * @param l
     */
	public Parser(List l){
		playlist = l;
	}

    /**
     * Parses a Youtube XML feed and creates VideoInfo objects that are stored in the List passed
     * as argument in the constructor.
     *
     * @param is        The InputSstream containing the Text to parse
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
	public void parse(InputStream is) throws ParserConfigurationException, 
													SAXException, IOException{
		SAXParser sp;		
		XMLReader xr;
		MyHandler m;
		sp = SAXParserFactory.newInstance().newSAXParser();
		xr = sp.getXMLReader();
		m = new MyHandler();
		InputSource ips = new InputSource(is);
		xr.setContentHandler(m);
		xr.parse(ips);
		
	}

    /**
     * Handler for parsing the data.
     */
	private class MyHandler extends DefaultHandler{
		private StringBuilder sb = new StringBuilder();
		private VideoInfo vi = null;

        /**
         * Empty constructor
         */
		public MyHandler(){
			
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes){
            /*
			System.out.println("---------START------------");
			System.out.println("Uri: " + uri);
			System.out.println("Localname: " + localName);
			System.out.println("qName: " + qName);
			System.out.println("Atributes: " + attributes);
			*/
			sb.setLength(0);

            // If a new item:
			if(qName.equalsIgnoreCase("item")){
				vi = new VideoInfo();
			}
		}
		
		public void characters(char[] ch, int start, int length){
			sb.append(ch,start,length);
		}
		
		public void endElement(String uri, String localName, String qName){
			/*
            System.out.println("------------END----------");
			System.out.println("Uri: " + uri);
			System.out.println("LocalName: " + localName);
			System.out.println("qName: " + qName);
			System.out.println("Value: " + sb);
			*/
			if (vi != null) {
				if (qName.equalsIgnoreCase("guid")){
					String[] sa;
					String link;
					sa = sb.toString().split(":");
					link = sa[sa.length - 1];
					vi.setYoutubeLink(link);
					vi.setImageLink("http://i.ytimg.com/vi/" + link +  "/mqdefault.jpg");
				} else if (qName.equalsIgnoreCase("title")) {
					vi.setTitle(sb.toString());
				} else if (qName.equalsIgnoreCase("item")) {
					playlist.add(vi);
				}
			}
		}
		
	}
}
