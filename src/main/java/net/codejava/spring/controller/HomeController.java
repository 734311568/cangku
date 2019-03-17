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

             
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
             
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
               
                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
             
                HttpEntity entity = response1.getEntity();
                if (entity != null) {

                        String str = EntityUtils.toString(entity, "UTF-8");
                        //��ȡ��������json����
                        JSONArray jsonArray = new JSONObject(str).getJSONArray("result");

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        Iterator<String> keys = jsonObject.keys();

                        for (int i = 0; i < jsonObject.length(); i++) {
                                //�õ����һ������еĽڵ�
                                String key1 = keys.next();
                                
                                String string1 = jsonObject.getString(key1);
                                //�����ڵ�
                                Element key1eElement = doc.createElement(key1);

                                if (!string1.endsWith("}") && !string1.endsWith("]")) {

                                        key1eElement.setTextContent(string1);
                                }
                                documentElement.appendChild(key1eElement);
                                //��json ����ʱ��
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
                                //����������
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

                        //���µ�����Ŀ
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

                //�����ĸ�Ԫ��
                Element documentElement = doc.createElement("document");
                doc.appendChild(documentElement);
                //�����ͻ���

                CloseableHttpResponse response1 = HttpClients.createDefault().execute(new HttpGet("https://redan-api.herokuapp.com/story/"));
                //�����ַ���ʵ��

                HttpEntity entity1 = response1.getEntity();
                if (entity1 != null) {
                        //�����ص�ʵ�����ñ���
                        String retSrc = EntityUtils.toString(entity1, "UTF-8");
                        //���������Ԫ�ص��Ǹ�����
                        JSONArray storyList = new JSONObject(retSrc).getJSONArray("result");

                        // ��ô���������Ԫ��ȡ�����е�Ԫ��
                        for (int i = 0; i < storyList.length(); i++) {
                                //�õ�json ÿ������Ԫ���ж�json�����ж�Ԫ���Ƿ����������
                                JSONObject jsonObject = storyList.getJSONObject(i);

                                Iterator<String> next1 = jsonObject.keys();

                                while (next1.hasNext()) {
                                        String next2 = next1.next();
                                        //�ı��������
                                        Element documentNext2 = doc.createElement(next2);
                                        String teString = jsonObject.get(next2).toString();
                                        if (!(next2.equals("author") || next2.equals("comments"))) {

                                                documentNext2.setTextContent(teString);

                                        }
                                        documentElement.appendChild(documentNext2);
                                        //��comments������
                                        if (next2.equals("comments")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray(next2);
                                                //������ǩ�ڵ�

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
                                                        //�����ڵ��ǩ
                                                        Element documentNext8 = doc.createElement(next8);
                                                        documentNext8.setTextContent(vaString2);

                                                        documentNext2.appendChild(documentNext8);
                                                }

                                        }

                                }

                        }

                }
                //���µ�����Ŀ
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
