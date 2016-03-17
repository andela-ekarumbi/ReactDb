package checkpoint.andela.utility;

import checkpoint.andela.models.Reaction;

public class ReactionHelper {

    public static void setReactionProperty(String propertyName,
                                           String propertyValue,
                                           Reaction reaction) {
        switch (propertyName) {
            case "UNIQUE-ID":
                reaction.setUniqueId(propertyValue);
                break;
            case "TYPES":
                reaction.setTypes(propertyValue);
                break;
            case "ATOM-MAPPINGS":
                reaction.setAtomMappings(propertyValue);
                break;
            case "CREDITS":
                reaction.setCredits(propertyValue);
                break;
            case "EC-NUMBER":
                reaction.setEcNumber(propertyValue);
                break;
            case "ENZYMATIC-REACTION":
                reaction.setEnzyMaticReaction(propertyValue);
                break;
            case "LEFT":
                reaction.setLeft(propertyValue);
                break;
            case "ORPHAN":
                reaction.setOrphan(propertyValue);
                break;
            case "PHYSIOLOGICALLY-RELEVANT":
                reaction.setPhysiologicallyRelevant(propertyValue);
                break;
            case "REACTION-DIRECTION":
                reaction.setReactionDirection(propertyValue);
                break;
            case "RIGHT":
                reaction.setRight(propertyValue);
                break;
            default:
                break;
        }
    }
}
