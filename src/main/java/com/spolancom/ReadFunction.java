package com.spolancom;
import java.io.*;
import java.util.*;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

/**
 * Class to Read variables or functions
 */
public class ReadFunction {
    private ArrayList<String> expressions = new ArrayList<>();
    /**
     * Dummy constructor
     */
    public ReadFunction(){
    }
    /**
     * readFile method
     */
    public Environment readFile(String path, Interpreter i) throws Exception{
        if(!path.startsWith("/")){
            path = System.getProperty("user.dir") + '/' + path;
        }
        //path = path.substring(1, path.length()-1);
        if(!(path.endsWith(".equ") || path.endsWith(".xlsx")))
            throw new EnvironmentException("Not valid file extension in file: " + path);
        if(path.endsWith(".equ")){
            //System.out.println(path);
            BufferedReader s = new BufferedReader(new FileReader(path));
            String line = "";

            while((line = s.readLine()) != null){
                expressions.add(line);
            }
            //Pass expressions to parser and put them into the environment
            Parser p = new Parser();
            Exp result;
            for(String exp : expressions){
                result = p.parse(exp);
                result.accept(i);
            }
            s.close();
        }
        else if(path.endsWith(".xlsx")){
            FileInputStream file = new FileInputStream(new File(path));
            XSSFWorkbook worksheet = new XSSFWorkbook(file);
            XSSFSheet sheet = worksheet.getSheetAt(0);
            Iterator<Row> rowIt = sheet.rowIterator();
            
            ArrayList<String> names = new ArrayList<String>();
            List<List<Double>> values = new ArrayList<List<Double>>();

            //-->Get indexes of table (column names)
            Iterator<Cell> c = rowIt.next().cellIterator();
            while(c.hasNext()){
                names.add(c.next().getStringCellValue());
            }
            names.remove(null);
            names.remove("");
            //<--

            //-->Get values of columns
            for( int j = 0; j < names.size(); j++){
                values.add(new ArrayList<>());
                while(rowIt.hasNext()){
                    Cell cell = rowIt.next().getCell(j);
                    if(cell != null){
                        if(cell.getCellType() == CellType.NUMERIC)
                            values.get(j).add(cell.getNumericCellValue());
                        else if(cell.getCellType() == CellType.NUMERIC && cell.getCachedFormulaResultType() == CellType.NUMERIC)
                            values.get(j).add(cell.getNumericCellValue());
                    }
                }
                rowIt = sheet.rowIterator();
                rowIt.next();
            }

            //Save data to environment
            for( int k = 0; k < names.size(); k++ ){
                i.envmnt.define(names.get(k), values.get(k));
            }
            worksheet.close();
            file.close();
        }
        return i.getEnv();
    }
}
