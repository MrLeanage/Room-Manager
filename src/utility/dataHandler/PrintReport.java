package utility.dataHandler;

import bean.Category;
import bean.Reservation;
import bean.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;
import utility.popUp.AlertPopUp;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class PrintReport extends JFrame {
//
  public void printReservation(Reservation reservation){
    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reservation.getReservedRoomList());
    try {
      HashMap parameter = new HashMap();
      parameter.put("address", AppConstant.BUSINESS_ADDRESS);
      parameter.put("contactNoInfo", AppConstant.BUSINESS_CONTACT_INFO);
      parameter.put("footerSlogan", AppConstant.BUSINESS_SLOGAN);
      parameter.put("reservationID", reservation.getResID());
      parameter.put("customerName", reservation.getResCustomerName());
      parameter.put("customerEmail", reservation.getResCustomerEmail());
      parameter.put("customerNIC", reservation.getResCustomerNIC());
      parameter.put("customerPhone", reservation.getResCustomerPhone());
      parameter.put("reservedDate", reservation.getResReservedDate());
      parameter.put("checkInDate", reservation.getResCheckInDate());
      parameter.put("checkOutDate", reservation.getResCheckOutDate());
      parameter.put("reservationStatus", reservation.getResStatus());

      parameter.put("roomList", jrBeanCollectionDataSource);


      JasperDesign jd =
          JRXmlLoader.load(
              new File("").getAbsolutePath() + "/src/view/jasperReport/reservation.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }

  public void printReservationList(ObservableList<Reservation> reservationObservableList, String reservationPeriod, String reservationStatus){
    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reservationObservableList);
    try {
      HashMap parameter = new HashMap();
      parameter.put("address", AppConstant.BUSINESS_ADDRESS);
      parameter.put("contactNoInfo", AppConstant.BUSINESS_CONTACT_INFO);
      parameter.put("footerSlogan", AppConstant.BUSINESS_SLOGAN);
      parameter.put("reservationPeriod", reservationPeriod);
      parameter.put("reservationStatus", reservationStatus);

      parameter.put("reservationList", jrBeanCollectionDataSource);


      JasperDesign jd =
              JRXmlLoader.load(
                      new File("").getAbsolutePath() + "/src/view/jasperReport/reservationList.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }


  public void printCategoryList(ObservableList<Category> categoryObservableList, String categoryType, String categoryAvailability){
    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(categoryObservableList);
    try {
      HashMap parameter = new HashMap();
      parameter.put("address", AppConstant.BUSINESS_ADDRESS);
      parameter.put("contactNoInfo", AppConstant.BUSINESS_CONTACT_INFO);
      parameter.put("footerSlogan", AppConstant.BUSINESS_SLOGAN);
      parameter.put("categoryType", categoryType.toUpperCase());
      parameter.put("categoryAvailability", categoryAvailability.toUpperCase());

      parameter.put("categoryList", jrBeanCollectionDataSource);


      JasperDesign jd =
              JRXmlLoader.load(
                      new File("").getAbsolutePath() + "/src/view/jasperReport/categoryList.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }

  public void printRoomList(ObservableList<Room> roomObservableList, String roomCategory, String roomAvailability, String reservationStatus){
    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(roomObservableList);
    try {
      HashMap parameter = new HashMap();
      parameter.put("address", AppConstant.BUSINESS_ADDRESS);
      parameter.put("contactNoInfo", AppConstant.BUSINESS_CONTACT_INFO);
      parameter.put("footerSlogan", AppConstant.BUSINESS_SLOGAN);
      parameter.put("roomCategory", roomCategory.toUpperCase());
      parameter.put("roomAvailability", roomAvailability.toUpperCase());
      parameter.put("reservationStatus", reservationStatus.toUpperCase());

      parameter.put("roomList", jrBeanCollectionDataSource);


      JasperDesign jd =
              JRXmlLoader.load(
                      new File("").getAbsolutePath() + "/src/view/jasperReport/roomList.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }

}
