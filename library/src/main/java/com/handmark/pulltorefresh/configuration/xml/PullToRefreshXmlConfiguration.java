package com.handmark.pulltorefresh.configuration.xml;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.internal.Utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 
 * @author NBP
 *
 */
public class PullToRefreshXmlConfiguration {

	private static class InstanceHolder {
		private final static PullToRefreshXmlConfiguration instance = new PullToRefreshXmlConfiguration();
		
		private static PullToRefreshXmlConfiguration getInstance() {
			return instance;
		}
	} 
	
	private static final int XML_PATH_ID = R.xml.pulltorefresh;
	private PullToRefreshNode node = null; 
	private boolean initialized = false;
	/**
	 * nothing to do
	 */
	private PullToRefreshXmlConfiguration() {}
	/**
	 * 
	 * @return
	 */
	public static PullToRefreshXmlConfiguration getInstance() {
		return InstanceHolder.getInstance();
	}
	/**
	 *  
	 * @param context
	 */
	public void init(Context context) {
		// get resources
		Resources resources = context.getResources();
		// read the file
		XmlPullParser parser = resources.getXml(XML_PATH_ID);
		// parser the xml
		XmlPullParserWrapper wrapper = new XmlPullParserWrapper(parser);
		
		try {
			node = new PullToRefreshConfigXmlParser(wrapper).parse();

			// load extended xml 
			XmlPullParser extendedXmlParser = ExtendedConfigXmlParserFactory.createParser(context);
			if ( extendedXmlParser != null) {
				XmlPullParserWrapper extendedXmlWrapper = new XmlPullParserWrapper(extendedXmlParser);
				// NOTE : if some exception throws from PullToRefreshConfigXmlParser, Extended xml will be skipped.
				PullToRefreshNode extendedNode = new PullToRefreshConfigXmlParser(extendedXmlWrapper).parse();
				node.extendProperties(extendedNode);
			}
		} catch (XmlPullParserException e) {
			Utils.error("It has failed to parse the xmlpullparser xml.\n " + e.getMessage());
		} catch (IOException e) {
			Utils.error("It has failed to parse the xmlpullparser xml.\n " + e.getMessage());
		}
//		catch (Exception e) {
//			Utils.error("It has failed to parse the xmlpullparser xml.\n " + e.getMessage());
//		}
		
		// Intialization can be done whether reading XML has failed or not! 
		initialized = true;
	}
	/**
	 * 
	 * @param layoutCode
	 * @return
	 */
	public String getLoadingLayoutClazzName(String layoutCode) {
		assertInitialized();
		if ( isNodeNull() ) {
			return null;
		}
		return node.loadingLayoutsNode.getLayoutClazzName(layoutCode);
	}
	/**
	 * 
	 * @param layoutCode
	 * @return
	 */
	public String getIndicatorLayoutClazzName(String layoutCode) {
		assertInitialized();
		if ( isNodeNull() ) {
			return null;
		}
		return node.indicatorLayoutsNode.getLayoutClazzName(layoutCode);
	}
	/**
	 * 
	 * @return
	 */
	private boolean isNodeNull() {
		return node == null;
	}
	/**
	 * 
	 * @return
	 */
	private boolean notInitialized() {
		return !initialized;
	}
	/**
	 * 
	 */
	private void assertInitialized() {
		if ( notInitialized() ) {
			throw new IllegalStateException(PullToRefreshXmlConfiguration.class.getName()+" has not initialized. Call init() method first.");
		}
	}
}
