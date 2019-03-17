package net.codejava.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Controller
public class HomeController {

        @RequestMapping(value = "/")
        public ModelAndView goHome(HttpServletResponse response) throws IOException {
                return new ModelAndView("home");
        }

        @RequestMapping(value = "/viewXSLT")
        public ModelAndView viewXSLT(HttpServletRequest request,
                HttpServletResponse response) throws IOException {
                // builds absolute path of the XML file
                String xmlFile = "resources/citizens.xml";
                String contextPath = request.getServletContext().getRealPath("");
                String xmlFilePath = contextPath + File.separator + xmlFile;

                Source source = new StreamSource(new File(xmlFilePath));

                // adds the XML source file to the model so the XsltView can detect
                ModelAndView model = new ModelAndView("XSLTView");
                model.addObject("xmlSource", source);

                return model;
        }

        @RequestMapping(value = "/viewXSLTtry")
        public ModelAndView viewXSLTtry(HttpServletRequest request,
                HttpServletResponse response) throws IOException {
                // builds absolute path of the XML file
                String xmlFile = "resources/myCDcollection_1.xml";
                String contextPath = request.getServletContext().getRealPath("");
                String xmlFilePath = contextPath + File.separator + xmlFile;

                Source source = new StreamSource(new File(xmlFilePath));

                // adds the XML source file to the model so the XsltView can detect
                ModelAndView model = new ModelAndView("XSLTViewTry");
                model.addObject("xmlSource", source);

                return model;
        }

        @RequestMapping(value = "/viewXSLTAccessLog")
        public ModelAndView viewXSLTAccessLog(HttpServletRequest request,
                HttpServletResponse response) throws IOException {
                // builds absolute path of the XML file
                String xmlFile = "resources/accessLog.xml";
                String contextPath = request.getServletContext().getRealPath("");
                String xmlFilePath = contextPath + File.separator + xmlFile;

                Source source = new StreamSource(new File(xmlFilePath));

                // adds the XML source file to the model so the XsltView can detect
                ModelAndView model = new ModelAndView("XSLTViewAccessLog");
                model.addObject("xmlSource", source);

                return model;
        }

        @RequestMapping(value = "/viewIterator")
        public void viewIterator(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {

                //创建解析DOM解析器
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                //根元素
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
                //响应数据
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
                //返回获取实体
                HttpEntity entity = response1.getEntity();
                if (entity != null) {

                        String str = EntityUtils.toString(entity, "UTF-8");
                        //把这个字符串转成json数组
                        JSONArray jsonArray = new JSONObject(str).getJSONArray("result");
                        //拿到第一个对象
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //通过第一个对象拿到所有的值
                        Iterator<String> keys = jsonObject.keys();
                        //
                        while (keys.hasNext()) {
                                //第一层所有key
                                String next = keys.next();

                                //拿到对象所对应的key所对应的value
                                String getKey = jsonObject.get(next).toString();
                                System.out.println("第一次的所有key-- " + next + "第一次的所有values" + getKey);
                                //  doc.createElement(getKey);
                                //是文本的情况下
                                Element element1 = doc.createElement(next);
                                if (!(getKey.endsWith("}") || getKey.endsWith("]"))) {
                                        //创建第一层的子节点

                                        System.out.println(next + "     " + getKey);
                                        element1.setTextContent(getKey);
                                }

                                documentElement.appendChild(element1);
                                if (getKey.endsWith("}")) {
                                        //第二层json对象
                                        JSONObject jsonObject2 = jsonObject.getJSONObject(next);
                                        //json对象的key
                                        Iterator<String> key1 = jsonObject2.keys();
                                        while (key1.hasNext()) {
                                                //拿到所有key
                                                String next1 = key1.next();
                                                String value2 = jsonObject2.getString(next1);

                                                Element element2 = doc.createElement(next1);

                                                element2.setTextContent(value2);
                                                element1.appendChild(element2);

                                        }

                                } //如果第json是数组
                                if (getKey.endsWith("]")) {

                                        JSONArray jsonArray1 = jsonObject.getJSONArray(next);
                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                                Iterator<String> key3 = jsonObject2.keys();
                                                while (key3.hasNext()) {
                                                        //拿到所有key
                                                        String next2 = key3.next();

                                                        String value3 = jsonObject2.get(next2).toString();

                                                        Element element3 = doc.createElement(next2);
                                                        if (!value3.endsWith("}")) {
                                                                element3.setTextContent(value3);

                                                        }
                                                        element1.appendChild(element3);
                                                        //判断第三层是否还是json对
                                                        if (value3.endsWith("}")) {
                                                                JSONObject jsonObject3 = jsonObject2.getJSONObject(next2);

                                                                Iterator<String> key4 = jsonObject3.keys();
                                                                while (key4.hasNext()) {
                                                                        String next5 = key4.next();
                                                                        //通过key拿到对应的valus
                                                                        String value = jsonObject3.getString(next5);
                                                                        System.out.println(next5 + "   第三层的的值 " + value);
                                                                        Element element4 = doc.createElement(next5);
                                                                        element4.setTextContent(value);

                                                                        element3.appendChild(element4);
                                                                }

                                                        }

                                                }
                                        }
                                }

                        }
                }

                //更新到本项目
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

        }

        @RequestMapping(value = "/viewFor")
        @ResponseBody
        public void viewFor(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {

             
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
             
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
               
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
             
                HttpEntity entity = response1.getEntity();
                if (entity != null) {

                        String str = EntityUtils.toString(entity, "UTF-8");
                        //获取的最外层的json数组
                        JSONArray jsonArray = new JSONObject(str).getJSONArray("result");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        Iterator<String> keys = jsonObject.keys();

                        for (int i = 0; i < jsonObject.length(); i++) {
                                //拿到这第一层的所有的节点
                                String key1 = keys.next();
                                
                                String string1 = jsonObject.getString(key1);
                                //创建节点
                                Element key1eElement = doc.createElement(key1);

                                if (!string1.endsWith("}") && !string1.endsWith("]")) {

                                        key1eElement.setTextContent(string1);
                                }
                                documentElement.appendChild(key1eElement);
                                //是json 对象时候
                                if (string1.startsWith("{")) {
                                        JSONObject jsono2 = jsonObject.getJSONObject(key1);

                                        Iterator<String> keys2 = jsono2.keys();
                                        for (int j = 0; j < jsono2.length(); j++) {
                                                String next2 = keys2.next();
                                                String vaString = jsono2.getString(next2);

                                                Element key3eElement = doc.createElement(next2);
                                                key3eElement.setTextContent(vaString);
                                                key1eElement.appendChild(key3eElement);
                                        }

                                }
                                //是数组的情况
                                if (string1.startsWith("[")) {

                                        JSONArray json3 = jsonObject.getJSONArray(key1);//鎵?鏈夊??

                                        for (int j = 0; j < json3.length(); j++) {
                                                JSONObject json4 = json3.getJSONObject(j);

                                                for (Iterator<String> keys3 = json4.keys(); keys3.hasNext();) {
                                                        String next4 = keys3.next();

                                                        String stringValue = json4.get(next4).toString();
                                                        Element key5Element = doc.createElement(next4);

                                                        key1eElement.appendChild(key5Element);

                                                        if (stringValue.startsWith("{")) {

                                                                JSONObject json5 = json4.getJSONObject(next4);
                                                                Iterator<String> keys4 = json5.keys();
                                                                for (int r = 0; r < json5.length(); r++) {
                                                                        String next5 = keys4.next();

                                                                        String stringValue2 = json5.getString(next5);

                                                                        Element key6Element = doc.createElement(next5);
                                                                        key6Element.setTextContent(stringValue2);

                                                                        key5Element.appendChild(key6Element);

                                                                }

                                                        } else if (!stringValue.startsWith("{")) {
                                                                System.out.println("key5" + next4 + "stringValue" + stringValue);
                                                                key5Element.setTextContent(stringValue);
                                                        }

                                                }
                                        }

                                }

                        }

                        //更新到本项目
                        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
                }

        }

        @RequestMapping(value = "/getView")
        @ResponseBody
        public void getView(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //创建的根元素
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
                //创建客户端

                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
                //返回字符串实体

                HttpEntity entity1 = response1.getEntity();
                if (entity1 != null) {
                        //给返回的实体设置编码
                        String retSrc = EntityUtils.toString(entity1, "UTF-8");
                        //获得最外层的元素的是个数组
                        JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");

                        // 那么遍历里面的元素取出所有的元素
                        for (int i = 0; i < storyList.length(); i++) {
                                //拿到json 每个数组元素判断json数组判断元素是否对象还是数组
                                JSONObject jsonObject = storyList.getJSONObject(i);

                                Iterator<String> next1 = jsonObject.keys();

                                while (next1.hasNext()) {
                                        String next2 = next1.next();
                                        //文本的情况下
                                        Element documentNext2 = doc.createElement(next2);
                                        String teString = jsonObject.get(next2).toString();
                                        if (!(next2.equals("author") || next2.equals("comments"))) {

                                                documentNext2.setTextContent(teString);

                                        }
                                        documentElement.appendChild(documentNext2);
                                        //在comments数组中
                                        if (next2.equals("comments")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray(next2);
                                                //创建标签节点

                                                for (int j = 0; j < jsonArray.length(); j++) {
                                                        JSONObject jsonObject3 = jsonArray.getJSONObject(j);
                                                        Iterator<String> next3 = jsonObject3.keys();
                                                        while (next3.hasNext()) {
                                                                String next4 = next3.next();
                                                                Element elementNext4 = doc.createElement(next4);
                                                                if (!next4.equals("who")) {
                                                                        String vaString = jsonObject3.getString(next4);
                                                                        elementNext4.setTextContent(vaString);

                                                                }
                                                                if (next4.equals("who")) {
                                                                        JSONObject jsonObject4 = jsonObject3.getJSONObject(next4);
                                                                        Iterator<String> next5 = jsonObject4.keys();
                                                                        while (next5.hasNext()) {
                                                                                String next6 = next5.next();
                                                                                String vaString = jsonObject4.getString(next6);
                                                                                Element documentNext6 = doc.createElement(next6);
                                                                                documentNext6.setTextContent(vaString);

                                                                                elementNext4.appendChild(documentNext6);
                                                                        }
                                                                }
                                                                documentNext2.appendChild(elementNext4);

                                                        }

                                                }

                                        }
                                        if (next2.equals("author")) {
                                                JSONObject jsonObject5 = jsonObject.getJSONObject(next2);
                                                Iterator<String> next7 = jsonObject5.keys();
                                                while (next7.hasNext()) {
                                                        String next8 = next7.next();
                                                        String vaString2 = jsonObject5.getString(next8);
                                                        //创建节点标签
                                                        Element documentNext8 = doc.createElement(next8);
                                                        documentNext8.setTextContent(vaString2);

                                                        documentNext2.appendChild(documentNext8);
                                                }

                                        }

                                }

                        }

                }
                //更新到本项目
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

        }

        @RequestMapping(value = "/viewXSL")

        public ModelAndView getXsl(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {
                // builds absolute path of the XML file
                String xmlFile = "resources/B.xml";
                String contextPath = request.getServletContext().getRealPath("");
                String xmlFilePath = contextPath + File.separator + xmlFile;

                Source source = new StreamSource(new File(xmlFilePath));

                // adds the XML source file to the model so the XsltView can detect
                ModelAndView model = new ModelAndView("B");
                model.addObject("xmlSource", source);

                return model;

        }
}
