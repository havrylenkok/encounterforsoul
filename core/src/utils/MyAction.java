package utils;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class MyAction {
	
	public static SequenceAction hideUi = new SequenceAction(Actions.alpha(1), Actions.scaleTo(0, 0, 2.8f, Interpolation.swingIn), Actions.alpha(0));
	public static SequenceAction showUi = new SequenceAction(Actions.scaleTo(0, 0),Actions.alpha(1), Actions.scaleTo(1, 1, 2.5f, Interpolation.swingIn));
	public static SequenceAction tap  = new SequenceAction(Actions.sequence(Actions.scaleTo(0.8f, 0.8f, 0.05f, Interpolation.linear),
															Actions.scaleTo(1f, 1f, 0.05f, Interpolation.linear)));

	public static SequenceAction dansButtons  = new SequenceAction(Actions.delay(0, Actions.forever(Actions.sequence(
			Actions.scaleTo(1f, 1f, 0.3f, Interpolation.fade),					
			Actions.scaleTo(1.02f, 0.98f, 0.3f, Interpolation.fade)
			))));


}
