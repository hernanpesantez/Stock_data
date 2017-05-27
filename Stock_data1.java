
//  Created by Hernan Pesantez on 5/16/17.
//  Copyright © 2017 Hernan Pesantez. All rights reserved.


import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

public class Stock_data1 {

    private String header = null;

    private String[] Sdata;

    private Vector stdDeviation = new Vector();
    private Vector date = new Vector();
    private Vector open = new Vector();
    private Vector high = new Vector();
    private Vector low = new Vector();
    private Vector close = new Vector();
    private Vector volume = new Vector();
    private Vector adjClose = new Vector();
    private Vector EMA = new Vector();
    private Vector SMA = new Vector();
    private float avg = 0;
    private float var = 0;

    //Default constructor
    public Stock_data1() {

    }

    public String getHeader() {
        return header;
    }
    public String getDate(int index) {
        return (String) date.get(index);
    }


    public void setHeader(String header) {
        this.header = header;
    }



    public static void main(String[] args) {

        // To instantiate an inner class, the outer class must be instantiate first.
        // Then, create the inner object within the outer object.

        Stock_data1 temp1 = new Stock_data1();


        Stock_data1.readFile temp2 = temp1.new readFile();
        Stock_data1.writeFile temp3 = temp1.new writeFile();


        //read the file
        temp2.read("/Users/hernanpesantez/Desktop/AMZN-2.csv");

        //compute
        temp1.stdDeviation();
        temp1.SMA();
        temp1.EMA();


        //write the file
        temp3.write("/Users/hernanpesantez/Desktop/AMZN-2.csv");
        System.out.println("Done");
        System.exit(0);

    }
    //Compute the Sample Moving Average
    public void SMA() {

        for (int m = 0; m < adjClose.size(); m++) {

            if (m >= 20) {
                for (int i = 0 + (m - 19); i <= m; i++) {

                    avg += (float) adjClose.get(i);
                }
                avg /= 20f;
                SMA.add(m, avg);
            } else {

                SMA.add(m, null);

            }
            avg = 0;
        }
    }
    //Compute the Exponential Moving Average
    public void EMA() {

        EMA.addElement(SMA.get(20));

        for (int i = 1; i < adjClose.size(); i++) {

            EMA.addElement((1 - (1.0f / 21.0f)) * (float) EMA.get(i - 1) + ((1.0f / 21f)) * (float) adjClose.get(i));

        }
    }

    //Compute the standard Deviation
    public void stdDeviation() {

        for (int m = 1; m < adjClose.size(); m++) {

            for (int i = 0; i <= m; i++) {

                avg += (float) adjClose.get(i);
            }

            avg = avg / m;

            for (int i = 1; i < m; i++) {
                var += ((float) adjClose.get(m) - avg) * ((float) adjClose.get(m) - avg);

            }

            var = (float) Math.abs(Math.sqrt(var / m));
            stdDeviation.add(m - 1, var);

            avg = 0;
            var = 0;
        }
        stdDeviation.insertElementAt(0.0, adjClose.size() - 1);

    }

    //Inner class used to read field

    public class readFile {

        //Default constructor
        public  readFile() {


        }


        public   void  read(String FILE){
            String currentLine;
            FileReader fr = null;
            BufferedReader br = null;

            try {
                fr = new FileReader(FILE);
                br = new BufferedReader(fr);

                int i = 0;
                while ((currentLine = br.readLine()) != null) {
                    ++i;


                    if(i==1){
                        Sdata=currentLine.split(",");

                        setHeader(Sdata[0]+","+Sdata[1]+","+Sdata[2]+","+Sdata[3]+","+Sdata[4]+","+Sdata[5]+","+Sdata[6]);
                    }
                    else if(i>1)
                    {
                        Sdata=currentLine.split(",");


                        date.addElement( Sdata[0]);
                        open.addElement( Float.valueOf(Sdata[1].trim()));
                        high.addElement(Float.valueOf(Sdata[2].trim()));
                        low.addElement( Float.valueOf(Sdata[3].trim()));
                        close.addElement( Float.valueOf(Sdata[4].trim()));
                        adjClose.addElement( Float.valueOf(Sdata[5].trim()));
                        volume.addElement(Float.valueOf(Sdata[6].trim()));

                    }
                }

                System.out.println("Reading file please wait...");

            } catch (IOException e) {
            }
        }
    }



    //Inner class used to write file
    public class writeFile {


        //Default constructor
        writeFile() {

        }


        public void write(String FILE){

           try{

               FileWriter fw=new FileWriter(FILE);
               BufferedWriter bw=new BufferedWriter(fw);


               //Add description for new columns
               bw.write(getHeader()+","+"Std Deviation"+","+"SMA"+","+"EMA");
               bw.newLine();
               for(int i=0; i< adjClose.size(); i++) {

                   bw.write(date.get(i)+","+open.get(i)+","+high.get(i)+","+low.get(i)+","+
                           close.get(i)+","+adjClose.get(i)+","+
                           volume.get(i)+","+stdDeviation.get(i)+","+SMA.get(i)+","+EMA.get(i));
                   bw.newLine();

               }
               System.out.println("Writing file please wait...");

               bw.close();
           }catch (IOException e){


          }
        }
    }
}



