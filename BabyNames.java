
/**
 * Write a description of BabyNames here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.*;
public class BabyNames {
    public void totalBirths(FileResource fr)
    {   int totalBirths=0,totalBoys=0,totalBoyNames=0;
        int totalNames=0,totalGirls=0,totalGirlNames=0;
        for(CSVRecord rec : fr.getCSVParser(false))
        {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalNames += 1;
            if(rec.get(1).equals("M"))
            {
                totalBoys += numBorn;
                totalBoyNames += 1;
            }
            else
            {
                totalGirls += numBorn;
                totalGirlNames += 1;
            }
        }
        System.out.println("Total Births = "+totalBirths);
        System.out.println("Total Boys = "+totalBoys);
        System.out.println("Total Girls = "+totalGirls);
        System.out.println("Total Names = "+totalNames);
        System.out.println("Total Unique Boy Names = "+totalBoyNames);
        System.out.println("Total Unique Girl Names = "+totalGirlNames);
    }
    public void testTotalBirths()
    {
        Scanner myObj = new Scanner(System.in);
        System.out.print("Enter year: ");
        int year = myObj.nextInt();
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+".csv");
        totalBirths(fr);
    }
    public int getRank(int year,String name,String Gender)
    {
        int rank=0;
        int f=0;
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+".csv");
        for(CSVRecord rec:fr.getCSVParser(false))
        {
            if(rec.get(1).equals(Gender))
            {
                if(rec.get(0).equals(name))
                {
                    f=1;
                    rank++;
                    break;
                }
                else    rank++;
            }
        }
        if(f==1)    return rank;
        else    return -1;
    }
    public void testGetRank()
    {
        /*Scanner myObj = new Scanner(System.in);
        System.out.println("Enter name,year and gender: ");
        String name = myObj.nextLine();
        //System.out.print("Enter year: ");
        int year = myObj.nextInt();
        //System.out.print("Enter gender: ");
        String gender = myObj.nextLine();*/
        int r = getRank(1960,"Emily","F");
        System.out.println("Rank "+r);
    }
    public String getName(int year,int rank,String gender)
    {
        String name;
        int cur=0,f=0;
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+".csv");
        for(CSVRecord rec:fr.getCSVParser(false))
        {
            if(rec.get(1).equals(gender))
            {
                cur++;
                if(cur==rank)
                {
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }
    public void testGetName()
    {
        String s = getName(1982,450,"M");
        System.out.println(s);
    }
    public void whatIsNameInYear(String name,int year,int newYear,String gender)
    {
        int r = getRank(year,name,gender);
        String newName = getName(newYear,r,gender);
        System.out.println(name+" born in "+year+" would be "+newName+
                            " if she was born in "+newYear+".");
    }
    public void testWhatIsNameInYear()
    {
        whatIsNameInYear("Owen",1972,2014,"M");
    }
    public int yearOfHighestRank(String name,String gender)
    {
        int mx=0; 
        int year=-1;
        DirectoryResource dr = new DirectoryResource();
        for(File f:dr.selectedFiles())
        {
            FileResource fr = new FileResource(f);
            String s = f.getAbsolutePath();
            //System.out.println(s);
            int ind = s.indexOf("yob");
            int currYear = Integer.parseInt(s.substring(ind+3,ind+7));
            int currRank = getRank(currYear,name,gender);
            if(mx==0 && currRank!=-1)
            {
                mx=currRank;
                year=currYear;
            }    
            else
            {
                if(mx>currRank)
                {
                    mx=currRank;
                    year=currYear;
                }    
            }
        }
        if(mx==0)   return -1;
        else    return year;
    }
    public void testYearOfHighestRank()
    {
        int highestRankYear = yearOfHighestRank("Mich","M");
        System.out.println("Highest Ranking of Mich was in "+highestRankYear);
    }
    public double getAverageRank(String name,String gender)
    {
        double avg=0.0;
        double cnt=0.0;
        DirectoryResource dr = new DirectoryResource();
        for(File f:dr.selectedFiles())
        {
            FileResource fr = new FileResource(f);
            String s = f.getAbsolutePath();
            int ind = s.indexOf("yob");
            int currYear = Integer.parseInt(s.substring(ind+3,ind+7));
            int currRank = getRank(currYear,name,gender);
            if(currRank!=-1)
            {
                avg+=currRank;
                cnt=cnt+1;
            }
        }
        if(cnt==0.0)  return -1.0;
        else
        {
            avg=avg/cnt;
            return avg;
        }
    }
    public void testGetAverageRank()
    {
        double a = getAverageRank("Robert","M");
        System.out.println(a);
    }
    public int getTotalBirthsRankedHigher(int year,String name,String gender)
    {
        FileResource fr = new FileResource("us_babynames_by_year/yob"+year+".csv");
        int rank = getRank(year,name,gender);
        if(rank==1) return 0;
        else
        {
            int i=1;
            int numBorn = 0;
            for(CSVRecord rec:fr.getCSVParser(false))
            {
                if(rec.get(1).equals(gender) && i<rank)
                {
                    numBorn += Integer.parseInt(rec.get(2));
                    i+=1;
                }
            }
            return numBorn;
        }
    }
    public void testGetTotalBirthsRankedHigher()
    {
        int num = getTotalBirthsRankedHigher(1990,"Emily","F");
        System.out.println(num);
    }
}
