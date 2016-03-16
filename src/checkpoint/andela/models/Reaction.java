package checkpoint.andela.models;

import java.util.List;

public class Reaction {

    private String uniqueId = "";

    private String types = "";

    private String atomMappings = "";

    private String credits = "";

    private String ecNumber = "";

    private String enzyMaticReaction = "";

    private String left = "";

    private String orphan = "";

    private String physiologicallyRelevant = "";

    private String reactionDirection = "";

    private String right = "";

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = setValue(this.uniqueId, uniqueId);
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = setValue(this.types, types);
    }

    public String getAtomMappings() {
        return atomMappings;
    }

    public void setAtomMappings(String atomMappings) {
        this.atomMappings = setValue(this.atomMappings, atomMappings);
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = setValue(this.credits, credits);
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = setValue(this.ecNumber, ecNumber);
    }

    public String getEnzyMaticReaction() {
        return enzyMaticReaction;
    }

    public void setEnzyMaticReaction(String enzyMaticReaction) {
        this.enzyMaticReaction = setValue(this.enzyMaticReaction, enzyMaticReaction);
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = setValue(this.left, left);
    }

    public String getOrphan() {
        return orphan;
    }

    public void setOrphan(String orphan) {
        this.orphan = setValue(this.orphan, orphan);
    }

    public String getPhysiologicallyRelevant() {
        return physiologicallyRelevant;
    }

    public void setPhysiologicallyRelevant(String physiologicallyRelevant) {
        this.physiologicallyRelevant
                = setValue(this.physiologicallyRelevant, physiologicallyRelevant);
    }

    public String getReactionDirection() {
        return reactionDirection;
    }

    public void setReactionDirection(String reactionDirection) {
        this.reactionDirection = setValue(this.reactionDirection, reactionDirection);
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = setValue(this.right, right);
    }

    private String setValue(String field, String value) {
        if (field.equals("")) {
            field += value;
        } else {
            field += "+plus;" + value;
        }
        return field;
    }
}
