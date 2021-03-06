package doconnor.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import doconnor.aop.domain.*;
import doconnor.aop.service.LeagueManager;

public class MainApp
{
  LeagueManager leagueManager;

  public static void main(String[] args)
  {
    new MainApp();
  }

  public MainApp()
  {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    leagueManager = (LeagueManager) context.getBean("leagueManagerImpl");
    System.out.println("Starting test1");
    test1();
    System.out.println("Starting test2");
    test2();
    System.out.println("Starting test3");
    test3();
    System.out.println("Starting test4");
    test4();
    System.out.println("Starting test5");
    test5();
  }

  private void test1()
  {
    Club club = new Club("testclub1");
    leagueManager.addClub(club, 1);
    club = new Club();
    // Should fail
    leagueManager.addClub(club, 1);
  }

  private void test2()
  {
    Club club = new Club("testclub1");
    club.setMainSponser(new Company("testCompany1"));
    leagueManager.addClub(club, 1);
    club.setMainSponser(new Company(""));
    // Should fail
    leagueManager.addClub(club, 1);
  }

  private void test3()
  {
    Player player = new Player("testplayer1");
    leagueManager.signPlayer(1, player);
    player = new Player("");
    // Should fail
    leagueManager.signPlayer(1, player);
    player = new Player("testplayer2");
    player.setAgent(new Agent(""));
    // Should fail
    leagueManager.signPlayer(1, player);
  }

  private void test4()
  {
    Club club = new Club("club1");
    club.getPlayers().add(new Player("testplayer1"));
    club.getPlayers().add(new Player("testplayer2"));
    leagueManager.addClub(club, 1);
    club = new Club("club2");
    club.getPlayers().add(new Player("testplayer1"));
    club.getPlayers().add(new Player());
    // Should fail
    leagueManager.addClub(club, 1);
  }

  private void test5()
  {
    Division division = new Division("Division 1");
    Club club = new Club("club1");
    club.getPlayers().add(new Player("testplayer1"));
    division.getMembers().add(club);
    leagueManager.setupDivision(division);
    club.getPlayers().add(new Player(""));
    // Should fail
    leagueManager.setupDivision(division);
  }

}
