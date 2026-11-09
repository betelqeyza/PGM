package tc.oc.pgm.action.actions;

import org.jetbrains.annotations.Nullable;
import tc.oc.pgm.action.ActionContext;
import tc.oc.pgm.api.feature.FeatureReference;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.controlpoint.ControlPoint;
import tc.oc.pgm.controlpoint.ControlPointDefinition;
import tc.oc.pgm.teams.TeamMatchModule;

public class ControlPointStateAction extends AbstractAction<Match> {

  public enum Owner {
    RED,
    BLUE,
    NEUTRAL
  }

  private final FeatureReference<? extends ControlPointDefinition> objective;
  private final Owner owner;

  public ControlPointStateAction(
      FeatureReference<? extends ControlPointDefinition> objective, Owner owner) {
    super(Match.class);
    this.objective = objective;
    this.owner = owner;
  }

  @Override
  public void trigger(Match match, ActionContext context) {
    ControlPoint controlPoint = (ControlPoint) this.objective.get().getGoal(match);
    if (controlPoint == null) return;

    @Nullable Competitor controller = null;
    if (this.owner != Owner.NEUTRAL) {
      String teamName = this.owner == Owner.RED ? "red" : "blue";
      controller = match.needModule(TeamMatchModule.class).getTeam(teamName);
      if (controller == null) {
        throw new IllegalStateException("Team '" + teamName + "' is not present in the match");
      }
    }

    controlPoint.setController(controller);
  }
}
