package com.example.testtaskxml.services;

import com.example.testtaskxml.DTO.CreditCardDto;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CreditCardService {

    private final JdbcTemplate jdbcTemplate;
    private String path="D:\\test";
    public CreditCardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        {
            try {
                createXmlFile();
                toZipFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createXmlFile() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Records");
        root.setAttribute("xmlns", "http://www.javacore.ru/schemas/");
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

            Element surName=doc.createElement("SurName");
            surName.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getSurname()));

            Element fathersName=doc.createElement("FathersName");
            fathersName.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getFathersname()));

            individual.appendChild(firstName);
            individual.appendChild(surName);
            individual.appendChild(fathersName);
            individual.appendChild(getElementWithAttribute(doc,"Classification","id","1"));
            individual.appendChild(getElementWithAttribute(doc,"Residency","id",card.getResident()));
            individual.appendChild(getElementWithTextContext(doc,"DateOfBirth",card.getDateOfBirth()));

            Element identifications=doc.createElement("Identifications");
            Element ident1=doc.createElement("Identification");
            ident1.appendChild(getElementWithTextContext(doc,"Number",card.getIdentification1()));
            ident1.appendChild(getElementWithTextContext(doc,"RegistrationDate",card.getIdent1_date_reg()));
            Element ident2=doc.createElement("Identification");
            ident2.appendChild(getElementWithTextContext(doc,"Number",card.getIdentification2()));
            ident2.appendChild(getElementWithTextContext(doc,"RegistrationDate",card.getIdent2_date_reg()));
            identifications.appendChild(ident1);
            identifications.appendChild(ident2);
            individual.appendChild(identifications);

            Element addresses=doc.createElement("Addresses");
            Element address1=doc.createElement("Address");
            address1.setAttribute("katoId",card.getKatoid1());
            Element streetName1=doc.createElement("StreetName");
            streetName1.appendChild(getElementWithAttributeAndText(doc,"Text","language","ru-RU",card.getStreetName1()));
            address1.appendChild(streetName1);


            Element address2=doc.createElement("Address");
            address1.setAttribute("katoId",card.getKatoid2());
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
            Element credit=getElementWithAttribute(doc,"Credit","paymentMethod","3");
            Element creditLimit=getElementWithAttributeAndText(doc,"CreditLimit","currency","KZT","WHAT IS IT?");
            Element records=doc.createElement("Records");

            Element record=getElementWithAttribute(doc,"Record","accountingDate",card.getAccountingDate());
            record.appendChild(getElementWithAttributeAndText(doc,"MonthlyInstalmentAmount","currency","KZT",card.getInstalmentAmount()));
            record.appendChild(getElementWithAttributeAndText(doc,"ResidualAmount","currency","KZT","0"));

            records.appendChild(record);
            credit.appendChild(creditLimit);
            credit.appendChild(records);
            type.appendChild(credit);
            contract.appendChild(general);
            contract.appendChild(type);
            root.appendChild(contract);
        }

        File file = new File(path+"\\test.xml");

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(file));
        System.out.println("good");
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
        try(
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path+"\\output.zip"));
            FileInputStream fis= new FileInputStream(path+"\\test.xml");
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
