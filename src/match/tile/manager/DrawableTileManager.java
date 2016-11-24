package match.tile.manager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;





import constants.TileConstants;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Dimension2D;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.layout.image.sprite.ISprite;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.layout.implementation.image.ImageDrawer;
import joe.game.layout.implementation.image.sprite.TileSprite;
import joe.game.platformer.match.IMatchManager;

public class DrawableTileManager extends AbstractTileManager {
	private ISprite fSprite;
	private Rectangle2D fDrawPosition;
	private boolean fIsBackgroundTile;
	
	public DrawableTileManager(IMatchManager matchManager, Map<String, Object> settings) {
		super(matchManager, settings);	
		String spriteLocation = Values.getStringValue(settings.get(TileConstants.Settings_SpriteLocation), null);
		if (spriteLocation != null) {
			fSprite = getSprite(spriteLocation);
		}
		
		fDrawPosition = Values.getValue(settings.get(TileConstants.Settings_DrawLocation), null, Rectangle2D.class);
		
		fIsBackgroundTile = Values.getBooleanValue(settings.get(TileConstants.Settings_IsBackground), false);
	}
	
	protected ISprite getSprite() {
		return fSprite;
	}
	
	protected Rectangle2D getDrawPosition() {
		return fDrawPosition;
	}
		
	@Override
	public void update(long time) {
		// Do Nothing
	}

	@Override
	public void draw(Graphics2D g, long time) {
		Rectangle2D drawPosition = getDrawPosition();
		ISprite sprite = getSprite();
		
		if (sprite != null && drawPosition != null) {
			if (!(sprite instanceof TileSprite)) {
				BufferedImage newImage = new BufferedImage((int)Math.abs(drawPosition.getWidth()), (int)Math.abs(drawPosition.getHeight()), BufferedImage.TYPE_INT_ARGB);
				Dimension2D dimension = sprite.getDimension();
				
				Graphics2D g2d = newImage.createGraphics();
				
				for (double x = 0; Math.abs(x) < Math.abs(drawPosition.getWidth()); x += dimension.getWidth()) {
					for (double y = 0; Math.abs(y) < Math.abs(drawPosition.getHeight()); y += dimension.getHeight()) {
						sprite.draw(g2d, LayoutCalculator.createRectangle(x, y, dimension.getWidth(), dimension.getHeight()));
					}
				}
				
				ImageDrawer.draw(g, newImage, drawPosition.getMinX(), drawPosition.getMinY(), drawPosition.getWidth(), drawPosition.getHeight());
			} else {
				sprite.draw(g, drawPosition);
			}
		}
	}

	@Override
	public boolean isBackgroundTile() {
		return fIsBackgroundTile;
	}
}
