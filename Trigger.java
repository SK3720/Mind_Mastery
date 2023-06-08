/** 
Class to handle physical walls the player can bump into

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue

*/

public class Trigger extends Hitbox {
    int id;

    public Trigger(int xPos, int yPos, int wid, int hgt, int i) {
        super(xPos, yPos, wid, hgt);
        id = i;
    }
    
    public String interactedBehaviour() {
        if (id == 2) return "goto 11";
        if (id == 3) return "paragraph \nOh hi, didn't see you there. *sigh*\nSchool hasn't been too good for me...\nI can't focus in class and it's really bringing down my grades.\nI need a better way to focus in the future...";
        if (id == 4) return "goto 13";
        if (id == 5) return "goto 12";
        if (id == 6) return "goto 14";
        if (id == 7) return "paragraph \nWelcome to our institute, where we explore the world of attention disorders. $ Focus in as we delve into valuable insights and strategies to understand and navigate the challenges they bring.\nAttention disorders, like ADHD, are conditions that affect a person's ability to $ pay attention and control impulsive behavior. These disorders can impact different areas $ of life, such as school and relationships.\nTasks such as following directions or completing tasks are a struggle for those $ who are affected by these disorders, which most heavily impact children. While $ these may seem to be simple tasks for most people, children affected by attention $ disorders are easily distracted and have trouble organizing their thoughts and actions.\nThe impacts of these disorders include below average academic performance, $ difficulty in social situations and relationships, and even low self-esteem $ as these children are not provided the support they need yet $ are still compared with their peers.\nIn order to survive Attention Disorders, various solutions can be implemented. $ Although medication is available, they are often accompanied by severe side effects, $ and cannot be used as a primary solution to these disorders. Thus, some effective $ and benefical solutions are as follows.\nCreating a Schedule: $\nBy creating a schedule, many benefits are acquired such as boosted productivity, $ increased focus, and an improvement in well being over all. $ Implementing a routine is essential to combat the impacts of attention disorders in daily life.";
        
        if (id == 10) return "goto 0";
        return "";
    }
    
    public String proximityMessage() {
        // learning sign 1
        if (id == 0) return "Welcome to the Learning Area! \n Head to the Red House to begin.";
        if (id == 1) return "wow";
        if (id == 2) return "Enter the Red House";
        if (id == 3) return "Talk to James";
        if (id == 4) return "Exit the Red House";
        if (id == 5) return "Enter the learning center";
        if (id == 6) return "Exit the learning center";
        if (id == 7) return "Talk with Pamela";
        
        
        return "";
    }
}
