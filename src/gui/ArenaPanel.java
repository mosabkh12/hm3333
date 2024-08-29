/*
Name:MohammedGhara,ID:213078116
Name:MosabShakar,ID:325103687
 */
package gui;

import game.arena.WinterArena;
import game.competition.WinterCompetition;
import game.entities.sportsman.WinterSportsman;
import game.enums.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ArenaPanel extends JPanel implements Runnable {
    private int arenaLength = 700;
    private int arenaWidth = 1000;
    private int maxCompetitors = 10;
    private String surface = null;
    private String weather = null;
    private String competition = null;
    private String discipline = null;
    private String league = null;
    private String gender = null;
    private int competitorsNumber = 0;
    private ImageIcon[] competitorsImages = null;
    private static ArrayList<WinterSportsman> competitors;
    private WinterArena arena = null;
    private WinterCompetition winterCompetition = null;
    private CompetitionFrame competitionFrame = null;
    private boolean competitionStarted = false;
    private boolean competitionFinished = false;
    private InfoTable infoTable = null;

    public void initArena() {

        this.removeAll();
        setPreferredSize(new Dimension(arenaWidth, arenaLength + 80));

        // Load the weather background image from the icons directory
        ImageIcon imageIcon1 = loadImage("/icons/" + weather + ".jpg");
        if (imageIcon1 != null) {
            Image scaledImage1 = imageIcon1.getImage().getScaledInstance(arenaWidth, arenaLength + 80, Image.SCALE_DEFAULT);
            JLabel picLabel1 = new JLabel(new ImageIcon(scaledImage1));
            picLabel1.setLocation(0, 0);
            picLabel1.setSize(arenaWidth, arenaLength + 80);
            add(picLabel1);

            // Add competitors' images to the panel
            for (int i = 0; i < competitorsNumber; i++) {
                JLabel picLabel2 = new JLabel(competitorsImages[i]);
                picLabel2.setLocation((int) competitors.get(i).getLocation().getY() + 5, (int) competitors.get(i).getLocation().getX());
                picLabel2.setSize(70, 70);
                picLabel1.add(picLabel2);
            }
        } else {
            System.err.println("Error: Image not found for weather: " + weather);
        }
    }

    public ArenaPanel() {
        setLayout(null);
          if (weather == null) {
        weather = "Sunny";
    }
        initArena();
    }

    public void buildArena(String surface, String weather) {
        this.surface = surface;
        this.weather = weather;

        competitors = new ArrayList<>();
        competitorsImages = new ImageIcon[maxCompetitors];
        winterCompetition = null;
        competition = null;
        maxCompetitors = 10;
        this.arenaWidth = 1000;

        SnowSurface snowSurf;
        WeatherCondition weatherCond;

        if (surface.equals("Powder"))
            snowSurf = SnowSurface.POWDER;
        else if (surface.equals("Crud"))
            snowSurf = SnowSurface.CRUD;
        else
            snowSurf = SnowSurface.ICE;

        if (weather.equals("Sunny"))
            weatherCond = WeatherCondition.SUNNY;
        else if (weather.equals("Cloudy"))
            weatherCond = WeatherCondition.CLOUDY;
        else
            weatherCond = WeatherCondition.STORMY;

        arena = new WinterArena(arenaLength, snowSurf, weatherCond);
        competitionFrame.updateFrame();
    }

    public void createCompetition(String competition, String discipline, String league, String gender) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.competition = competition;
        this.discipline = discipline;
        this.league = league;
        this.gender = gender;

        competitionStarted = competitionFinished = false;
        competitorsNumber = 0;

        int newWidth = (maxCompetitors) * 75 + 5;

        if (newWidth > 1000)
            this.arenaWidth = newWidth;
        else
            this.arenaWidth = 1000;

        competitors = new ArrayList<>();
        competitorsImages = new ImageIcon[maxCompetitors];

        Discipline disc;
        League leag;
        Gender gen;

        if (discipline.equals("Slalom"))
            disc = Discipline.SLALOM;
        else if (discipline.equals("Giant-Slalom"))
            disc = Discipline.GIANT_SLALOM;
        else if (discipline.equals("Downhill"))
            disc = Discipline.DOWNHILL;
        else
            disc = Discipline.FREESTYLE;

        if (league.equals("Junior"))
            leag = League.JUNIOR;
        else if (league.equals("Adult"))
            leag = League.ADULT;
        else
            leag = League.SENIOR;

        if (gender.equals("Male"))
            gen = Gender.MALE;
        else
            gen = Gender.FEMALE;

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> c = cl.loadClass("game.competition." + competition + "Competition");
        Constructor<?> con = c.getConstructor(WinterArena.class, int.class, Discipline.class, League.class, Gender.class);

        winterCompetition = (WinterCompetition) con.newInstance(arena, maxCompetitors, disc, leag, gen);

        competitionFrame.updateFrame();
    }

    public void addCompetitor(String name, double age, double maxSpeed, double acceleration) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        WinterSportsman ws = null;

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> c = cl.loadClass("game.entities.sportsman." + competition + "er");
        Constructor<?> con = c.getConstructor(String.class, double.class, Gender.class, double.class, double.class, Discipline.class);

        ws = (WinterSportsman) con.newInstance(name, age, winterCompetition.getGender(), acceleration, maxSpeed, winterCompetition.getDiscipline());

        try {
            winterCompetition.addCompetitor(ws);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Competitor does not fit to competition! Choose another competitor.");
            return;
        }
        competitors.add(ws);

        // Load the competitor's image from the icons directory
        String imagePath = "/icons/" + competition + gender + ".png";
        ImageIcon icon = loadImage(imagePath);
        if (icon != null) {
            competitorsImages[competitorsNumber] = new ImageIcon(icon.getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
            competitorsNumber++;
            competitionFrame.updateFrame();
        } else {
            System.err.println("Error: Image not found for competitor: " + competition + gender);
        }
    }

    public void startRace() {
        competitionStarted = true;

        try {
            new Thread(this).start();
            winterCompetition.startCompetition();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void showInfo() {
        if (infoTable != null)
            infoTable.dispose();

        infoTable = new InfoTable(winterCompetition, competitorsNumber);
    }

    public void setArenaLength(int arenaLength) {
        this.arenaLength = arenaLength;
    }

    public int getArenaLength() {
        return arenaLength;
    }

    public void setArenaWidth(int arenaWidth) {
        this.arenaWidth = arenaWidth;
    }

    public int getArenaWidth() {
        return arenaWidth;
    }

    public String getWeather() {
        return this.weather;
    }

    public String getSurface() {
        return this.surface;
    }

    public String getDiscipline() {
        return discipline;
    }

    public String getLeague() {
        return league;
    }

    public String getGender() {
        return gender;
    }

    public void setMaxCompetitors(int maxCompetitors) {
        this.maxCompetitors = maxCompetitors;
    }

    public int getMaxCompetitors() {
        return this.maxCompetitors;
    }

    public boolean noArena() {
        return arena == null;
    }

    public boolean fullArena() {
        return competitorsNumber == maxCompetitors;
    }

    public boolean noCompetitors() {
        return competitorsNumber == 0;
    }

    public void setCompetitionFrame(CompetitionFrame competitionFrame) {
        this.competitionFrame = competitionFrame;
    }

    public boolean isCompetitionStarted() {
        return this.competitionStarted;
    }

    public boolean isCompetitionFinished() {
        return this.competitionFinished;
    }

    public String getCompetition() {
        return competition;
    }

    @Override
    public void run() {
        while (winterCompetition.hasActiveCompetitors()) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            try {
                competitionFrame.updateFrame();
            } catch (Exception e) {
            }
        }
        competitionFrame.updateFrame();
        competitionFinished = true;
    }


    private ImageIcon loadImage(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            return null;
        }
    }
}
