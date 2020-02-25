/* Starting code for Assignment 1
 * Student: Jose Soto
 * CISC 3130-MY9
 * Brooklyn College, Spring 2020
 *
 * Took US based list, had issues with inputting lines that had multiple commas, as initial attempts were using the
 * commas separating the data to split lines but many artist names in this list starting at 68 have commas in the artist
 * field.  Truncated the updated list to 65 to get the concept going.
 */
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

class Single { //Single will take in all the elements provided in the csv file
    int position;
    String track;
    String artist;
    int streams;
    String url;
    Single next;

    Single(int ps, String st, String sa, int ss, String su){
        position = ps;
        track = st;
        artist = sa;
        streams = ss;
        url = su;
    }

    void displayLink()
    {
        System.out.printf("%d %-60s %-20s %-15d %-40s%n", position, track, artist, streams, url);
        //Formated with print f to maintain similar spacing in console window due to variances in track titles/artist names
    }
}//end of Single class

/* The List SortedArtists is composed of a series of artist names */
class SortedArtists<size> {
    private Single first;
    private Single last;

    private SortedArtists(){
        first = null;
    }
    private SortedArtists(Single[] linkArr){
        first = null; // initialize list
        for(int j=0; j<linkArr.length; j++)
            insertSorted(linkArr[j]);
    }

    private boolean isEmpty(){
        return (first == null);
    }

    private void insertSorted(Single k)//Sorts by artists name as be request in assignment
    { // make new link
        Single previous = null; // start at first
        Single current = first; // until end of list,
        while(current != null && k.artist.compareTo(current.artist) > 0)
        { // or key > current,
            previous = current;
            current = current.next; // go to next item
        }
        if(previous==null) // at beginning of list
            first = k; // first --> k
        else // not at beginning
            previous.next = k; // old prev --> k
        k.next = current; // k --> old current
    }

    public void insertFirst(int ps, String st, String sa, int ss, String su)//adds to top of the list, never used but kept for testing/flexibility
    { // make new link
        Single newLink = new Single(ps, st, sa, ss, su);
        newLink.next = first; // newLink --> old first
        first = newLink; // first --> newLink
    }
    private void insertLast(int ps, String st, String sa, int ss, String su)//adds to bottom of the list
    {
        Single newLink = new Single(ps, st, sa, ss, su);
        if( isEmpty() ) // if empty list,
            first = newLink; // first --> newLink
        else
            last.next = newLink; // old last --> newLink
        last = newLink; // newLink <-- last
    }
    public Single deleteFirst() // delete first item, never used but kept for testing/flexibility
    { // (assumes list not empty)
        Single temp = first; // save reference to link
        first = first.next; // delete it: first-->old next
        return temp; // return deleted link
    }

    public void displayList()
    {
        Single current = first; // start at beginning of list
        while(current != null) // until end of list,
        {
            current.displayLink(); // print data
            current = current.next; // move to next link
            System.out.println("");
        }
        System.out.println("");
    }
    private Single remove() // return & delete first link, used after creating a sorted Link list using a established array of data.
    { // (assumes non-empty list)
        Single temp = first; // save first
        first = first.next; // delete first
        return temp; // return value
    }

    public static void main(String [ ] args) {
        SortedArtists artistNames = new SortedArtists();
        String csvFile = "C:\\Users\\franc\\IdeaProjects\\SortedArtists\\src\\regional-us-weekly-2020-01-17--2020-01-24.csv";
        int counter = 0, size = 65; //counter is for incrementing in while loop, size is size of data from the file

        Single[] linkArray = new Single[size]; //array of type Single to hold values that are being inserted into linked list artistNames during loop
        try {

            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] inputLine;

            while ((inputLine = reader.readNext()) != null) {

                int tempPosition = Integer.parseInt(inputLine[0]);
                String tempTrack = inputLine[1];
                String tempArtist = inputLine[2];
                int tempStreams = Integer.parseInt(inputLine[3]);
                String tempUrl = inputLine[4];
                /*tempPosition and tempStreams are using parseInt as all data split into the tempArray of type String are
                * made into String types, for ease of us they are converted back into Int to make manipulating them
                * easier if necessary.  The other temp files are just placeholders to later insert, as we know from the
                * csv file in which position they are being read from*/

                artistNames.insertLast(tempPosition, tempTrack, tempArtist, tempStreams, tempUrl);//So the linked list
                // is being fed data in descending order from position, no sorting at this time

                linkArray[counter] = new Single(tempPosition, tempTrack, tempArtist, tempStreams, tempUrl);/*stores a object
                * of Single into current position of linkArray*/
                counter++;
            }
            reader.close();//stops reading file after while loop
        }catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }catch(NumberFormatException ex) {//Error handling incase wrong data is run through parseInt
            System.out.println("Error. Please input a valid number :-");
        }

       /*Leftover Test code to make sure items saved to main array correctly, and that .displayList() shows the
         linked list was fed data correctly
        System.out.printf("%d %-60s %-20s %-15d %-40s%n", linkArray[0].position, linkArray[0].track,
                linkArray[0].artist, linkArray[0].streams, linkArray[0].url);
        System.out.printf("%d %-60s %-20s %-15d %-40s%n", linkArray[6].position, linkArray[6].track,
                linkArray[6].artist, linkArray[6].streams, linkArray[6].url);
        artistNames.displayList();*/
        for(int j = 0; j<size; j++){
            System.out.printf("%d %-60s %-20s %-15d %-40s%n", linkArray[j].position, linkArray[j].track,
                    linkArray[j].artist, linkArray[j].streams, linkArray[j].url);
        }//Formatted printing of unmodified linked list, same output as artistNames.displayList()

        SortedArtists sortedList = new SortedArtists(linkArray); /*created new linked list sortedList, feeding the saved
                                                                 array into a separate constructor*/
        for(int j = 0; j<size; j++){
            linkArray[j] = sortedList.remove(); //deletes first line from beginning of sorted link and saves it into array
        }
        for(int j = 0; j<size; j++){
            System.out.printf("%d %-60s %-20s %-15d %-40s%n", linkArray[j].position, linkArray[j].track,
                    linkArray[j].artist, linkArray[j].streams, linkArray[j].url);
        }//Formatted printing of sorted linked list from sortedList
    }//end of main
}//end of SortedArtists class


