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

                //��������DOM������
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                //��Ԫ��
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
                //��Ӧ����
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
                //���ػ�ȡʵ��
                HttpEntity entity = response1.getEntity();
                if (entity != null) {

                        String str = EntityUtils.toString(entity, "UTF-8");
                        //������ַ���ת��json����
                        JSONArray jsonArray = new JSONObject(str).getJSONArray("result");
                        //�õ���һ������
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //ͨ����һ�������õ����е�ֵ
                        Iterator<String> keys = jsonObject.keys();
                        //
                        while (keys.hasNext()) {
                                //��һ������key
                                String next = keys.next();

                                //�õ���������Ӧ��key����Ӧ��value
                                String getKey = jsonObject.get(next).toString();
                                System.out.println("��һ�ε�����key-- " + next + "��һ�ε�����values" + getKey);
                                //  doc.createElement(getKey);
                                //���ı��������
                                Element element1 = doc.createElement(next);
                                if (!(getKey.endsWith("}") || getKey.endsWith("]"))) {
                                        //������һ����ӽڵ�

                                        System.out.println(next + "     " + getKey);
                                        element1.setTextContent(getKey);
                                }

                                documentElement.appendChild(element1);
                                if (getKey.endsWith("}")) {
                                        //�ڶ���json����
                                        JSONObject jsonObject2 = jsonObject.getJSONObject(next);
                                        //json�����key
                                        Iterator<String> key1 = jsonObject2.keys();
                                        while (key1.hasNext()) {
                                                //�õ�����key
                                                String next1 = key1.next();
                                                String value2 = jsonObject2.getString(next1);

                                                Element element2 = doc.createElement(next1);

                                                element2.setTextContent(value2);
                                                element1.appendChild(element2);

                                        }

                                } //�����json������
                                if (getKey.endsWith("]")) {

                                        JSONArray jsonArray1 = jsonObject.getJSONArray(next);
                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                                Iterator<String> key3 = jsonObject2.keys();
                                                while (key3.hasNext()) {
                                                        //�õ�����key
                                                        String next2 = key3.next();

                                                        String value3 = jsonObject2.get(next2).toString();

                                                        Element element3 = doc.createElement(next2);
                                                        if (!value3.endsWith("}")) {
                                                                element3.setTextContent(value3);

                                                        }
                                                        element1.appendChild(element3);
                                                        //�жϵ������Ƿ���json��
                                                        if (value3.endsWith("}")) {
                                                                JSONObject jsonObject3 = jsonObject2.getJSONObject(next2);

                                                                Iterator<String> key4 = jsonObject3.keys();
                                                                while (key4.hasNext()) {
                                                                        String next5 = key4.next();
                                                                        //ͨ��key�õ���Ӧ��valus
                                                                        String value = jsonObject3.getString(next5);
                                                                        System.out.println(next5 + "   ������ĵ�ֵ " + value);
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

                //���µ�����Ŀ
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

        }

        @RequestMapping(value = "/viewFor")
        @ResponseBody
        public void viewFor(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ParserConfigurationException, TransformerException {

                //创建解析DOM解析�?
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                //根元�?
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
                        //拿到�?外成的个对象
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //通过第一个对象拿到所有的�?
                        Iterator<String> keys = jsonObject.keys();

                        for (int i = 0; i < jsonObject.length(); i++) {
                                //拿到result这层的所有key
                                String key1 = keys.next();
                                //通过key拿到�?,然针对�?�再来判断一下是数组还是对象或�?�是文本
                                String string1 = jsonObject.get(key1).toString();
                                //打印第一层key

                                Element key1eElement = doc.createElement(key1);

                                if (!string1.endsWith("}") && !string1.endsWith("]")) {
                                        System.out.println("是文�?" + string1 + "     " + key1);
                                        key1eElement.setTextContent(string1);
                                }
                                documentElement.appendChild(key1eElement);
                                //是json数组的对象时�?
                                if (string1.startsWith("{")) {
                                        JSONObject jsono2 = jsonObject.getJSONObject(key1);//拿到�?有的�?
                                        // Iterator<String> keys2 = jsono2.keys();//�?有的�?
                                        Iterator<String> keys2 = jsono2.keys();
                                        for (int j = 0; j < jsono2.length(); j++) {
                                                String next2 = keys2.next();
                                                String vaString = jsono2.getString(next2);

                                                Element key3eElement = doc.createElement(next2);
                                                key3eElement.setTextContent(vaString);
                                                key1eElement.appendChild(key3eElement);
                                        }

                                }
                                //是json数组的情况下
                                if (string1.startsWith("[")) {

                                        JSONArray json3 = jsonObject.getJSONArray(key1);//�?有�??

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
                                                                        //   String key6 = keys4.next();
                                                                        String stringValue2 = json5.getString(next5);

                                                                        System.out.println("key6" + next5 + "stringValue2" + stringValue2);

                                                                        Element key6Element = doc.createElement(next5);
                                                                        key6Element.setTextContent(stringValue2);
                                                                        // key6Element.appendChild(key6Element);
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

                        //���µ�����Ŀ
                        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
                }

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
