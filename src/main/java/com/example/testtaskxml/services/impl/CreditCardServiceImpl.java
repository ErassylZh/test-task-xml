package com.example.testtaskxml.services.impl;

import com.example.testtaskxml.DTO.CreditCardDto;
import com.example.testtaskxml.services.CreditCardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final JdbcTemplate jdbcTemplate;
    private OutputStream outputStream;
    private InputStream inputStream;
    public CreditCardServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

            try {
                createXmlFile();
                toZipFile();
                System.out.println(encodeZipFile());
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public Document createXmlFile() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();

        System.out.println("before  "+doc.getChildNodes());

        Element root = doc.createElement("Records");
        root.setAttribute("xmlns", "http://www.datapump.cig.com");
        root.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xs:schemaLocation", "http://www.datapump.cig.com SRC_Contract_KZ_v5_guar.xsd");

        doc.appendChild(root);
        for (CreditCardDto card:getAllData()){
            Element contract=doc.createElement("Contract");
            contract.setAttribute("operation","1");

            Element general=doc.createElement("General");
            general.appendChild(getElementWithTextContext(doc,"ContractCode",card.getContractCode()));
            general.appendChild(getElementWithTextContext(doc,"AgreementNumber",card.getAgreementNumber()));
            general.appendChild(getElementWithAttribute(doc,"FundingType","id",card.getFundingType()));
            general.appendChild(getElementWithAttribute(doc,"CreditPurpose2","id",card.getCreditPurpose2()));
            general.appendChild(getElementWithAttribute(doc,"CreditObject","id",card.getCreditObject()));
            general.appendChild(getElementWithAttribute(doc,"ContractPhase","id",card.getContractPhase()));
            general.appendChild(getElementWithAttribute(doc,"ContractStatus","id",card.getContractStatus()));
            general.appendChild(getElementWithTextContext(doc,"StartDate",card.getStartDate()));
            general.appendChild(getElementWithTextContext(doc,"EndDate",card.getEndDate()));
            general.appendChild(getElementWithTextContext(doc,"AnnualEffectiveRate",card.getAnnualEffectiveRate()));
            general.appendChild(getElementWithTextContext(doc,"NominalRate",card.getNominalRate()));

            Element subjects=doc.createElement("Subjects");

            Element subject=doc.createElement("Subject");
            subject.setAttribute("roleId","1");
            Element entity=doc.createElement("Entity");
            Element individual=doc.createElement("Individual");

            Element firstName=doc.createElement("FirstName");
            firstName.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getFirstname()));

            Element surname=doc.createElement("Surname");
            surname.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getSurname()));

            Element fathersName=doc.createElement("FathersName");
            fathersName.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getFathersname()));

            individual.appendChild(firstName);
            individual.appendChild(surname);
            individual.appendChild(fathersName);
            individual.appendChild(getElementWithAttribute(doc,"Classification","id","1"));
            individual.appendChild(getElementWithAttribute(doc,"Residency","id",card.getResident()));
            individual.appendChild(getElementWithTextContext(doc,"DateOfBirth",card.getDateOfBirth()));

            Element identifications=doc.createElement("Identifications");
            Element ident1=doc.createElement("Identification");
            ident1.setAttribute("typeId","7");
            ident1.setAttribute("rank","2");
            ident1.appendChild(getElementWithTextContext(doc,"Number",card.getIdentification1()));
            ident1.appendChild(getElementWithTextContext(doc,"RegistrationDate",card.getIdent1_date_reg()));

            Element ident2=doc.createElement("Identification");
            ident2.setAttribute("typeId","7");
            ident2.setAttribute("rank","2");
            ident2.appendChild(getElementWithTextContext(doc,"Number",card.getIdentification2()));
            ident2.appendChild(getElementWithTextContext(doc,"RegistrationDate",card.getIdent2_date_reg()));
            identifications.appendChild(ident1);
            identifications.appendChild(ident2);
            individual.appendChild(identifications);

            Element addresses=doc.createElement("Addresses");
            Element address1=doc.createElement("Address");
            address1.setAttribute("typeId","1");
            address1.setAttribute("katoId",card.getKatoid1());
            Element streetName1=doc.createElement("StreetName");
            streetName1.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getStreetName1()));
            address1.appendChild(streetName1);


            Element address2=doc.createElement("Address");
            address2.setAttribute("typeId","1");
            address2.setAttribute("katoId",card.getKatoid2());
            Element streetName2=doc.createElement("StreetName");
            streetName2.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getStreetName2()));
            address2.appendChild(streetName2);

            addresses.appendChild(address1);
            addresses.appendChild(address2);

            individual.appendChild(addresses);
            entity.appendChild(individual);
            subject.appendChild(entity);
            subjects.appendChild(subject);
            general.appendChild(subjects);

            //-Type
            Element type=doc.createElement("Type");
            Element installment=getElementWithAttribute(doc,"Instalment","paymentPeriodId","2");
            Element totalAmount=getElementWithAttributeAndText(doc,"TotalAmount","currency","KZT",card.getTotalAmount());
            Element installmentAmount=getElementWithAttributeAndText(doc,"InstalmentAmount","currency","KZT",card.getInstalmentAmount());
            Element records=doc.createElement("Records");

            Element record=getElementWithAttribute(doc,"Record","accountingDate",card.getAccountingDate());
            record.appendChild(getElementWithAttributeAndText(doc,"OutstandingAmount","currency","KZT",card.getOutstanding_amount()));

            records.appendChild(record);
            installment.appendChild(totalAmount);
            installment.appendChild(installmentAmount);
            installment.appendChild(records);

            type.appendChild(installment);
            contract.appendChild(general);
            contract.appendChild(type);
            root.appendChild(contract);

//            create file for test xml!
//
        }
//

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(F));
        System.out.println("good");
        System.out.println("after "+doc);
        return doc;
    }

    private Element getElementWithAttributeAndText(Document doc,String tagName, String attributeName,String attributeValue,String contentText){
        Element res=doc.createElement(tagName);
        res.setAttribute(attributeName,attributeValue);
        res.setTextContent(contentText);
        return res;
    }
    private Element getElementWithAttribute(Document doc,String tagName, String attributeName,String attributeValue){
        Element res=doc.createElement(tagName);
        res.setAttribute(attributeName,attributeValue);
        return res;
    }
    private Element getElementWithTextContext(Document doc,String tagName, String contentText){
        Element res=doc.createElement(tagName);
        res.setTextContent(contentText);
        return res;
    }
    public void toZipFile(){
        try
                (
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream("D:\\codes\\Java codes\\TestTaskXML\\src\\main\\resources\\XMLData\\output.zip"));
                FileInputStream fis= new FileInputStream("D:\\codes\\Java codes\\TestTaskXML\\src\\main\\resources\\XMLData\\test.xml");
        )
        {
            ZipEntry entry=new ZipEntry("text.xml");
            out.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            out.write(buffer);
            out.closeEntry();
            System.out.println("ZIP FILE CREATED!!!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public ByteArrayOutputStream xmlToZipFile(){
        try {
            // Create a new Document object
            Document document = createXmlFile();

            // Create a new ZipOutputStream
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

            // Create a new ZipEntry for the XML file
            ZipEntry xmlEntry = new ZipEntry("cards.xml");
            zipOutputStream.putNextEntry(xmlEntry);

            // Transform the Document object to a Source object
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            // Write the Source object to the ZipOutputStream
            StreamResult result = new StreamResult(zipOutputStream);
            transformer.transform(source, result);

            // Close the ZipEntry and ZipOutputStream
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            return byteArrayOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encodeZipFile(){

        byte[] zipBytes = xmlToZipFile().toByteArray();

        // Encode the zip file contents using Base64
        String base64Encoded = Base64.getEncoder().encodeToString(zipBytes);

        return base64Encoded;
    }


    public List<CreditCardDto> getAllData(){
        String query="SELECT * FROM fcb_daily_report_fields2";
        return jdbcTemplate.query(query,(rs,rownum)->
                    new CreditCardDto(
                            rs.getString("contractCode"),
                            rs.getString("agreementNumber"),
                            rs.getString("findingType"),
                            rs.getString("creditPurpose2"),
                            rs.getString("creditObject"),
                            rs.getString("contractPhrase"),
                            rs.getString("contractStatus"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getString("annualEffectiveRate"),
                            rs.getString("nominalRate"),
                            rs.getString("paymentPeriod"),
                            rs.getString("totalAmount"),
                            rs.getString("instalmentAmount"),
                            rs.getString("outstanding_amount"),
                            rs.getString("firstname"),
                            rs.getString("surname"),
                            rs.getString("fathersname"),
                            rs.getString("dateOfBirth"),
                            rs.getString("identification1"),
                            rs.getString("ident1_date_reg"),
                            rs.getString("identification2"),
                            rs.getString("katoid1"),
                            rs.getString("katoid2"),
                            rs.getString("streetName1"),
                            rs.getString("streetName2"),
                            rs.getString("accountingDate"),
                            rs.getString("ident2_date_reg"),
                            rs.getString("resident"),
                            rs.getString("fcb_status"),
                            rs.getString("scb_status"),
                            rs.getString("batch_fcb"),
                            rs.getString("batch_scb")
                    )
                );
    }
}
