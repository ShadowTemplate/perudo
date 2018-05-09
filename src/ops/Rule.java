package ops;

public class Rule {

    private String rule;
    private String salience;

    public Rule(String rule, String salience) {
        this.rule = rule;
        this.salience = salience;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getSalience() {
        return salience;
    }

    public void setSalience(String salience) {
        this.salience = salience;
    }

    @Override
    public String toString() {
        return rule + "~" + salience;
    }

}
