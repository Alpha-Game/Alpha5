package match.effect;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import joe.classes.base.HashMapList;
import joe.classes.identifier.IMappable;
import joe.game.base.effect.IEffect;
import joe.game.base.effect.IEffectableObject;
import joe.game.base.effect.IStateType;

public class Effects implements IEffectableObject {
	protected Map<String, HashMapList<IEffect, Long>> fBaseEffects;
	protected Map<ComparisonDouble, HashMapList<IEffect, Long>> fEffects;
	protected Map<IEffect, ComparisonDouble> fEffectPriority;
	
	public Effects() {
		fBaseEffects = new HashMap<String, HashMapList<IEffect, Long>>();
		fEffects = new TreeMap<ComparisonDouble, HashMapList<IEffect, Long>>(new Comparator<ComparisonDouble>() {
			@Override
			public int compare(ComparisonDouble paramT1, ComparisonDouble paramT2) {
				return paramT1.compare(paramT2);
			}
		});
		fEffectPriority = new HashMap<IEffect, ComparisonDouble>();
	}
	
	public void addBaseEffect(IEffect effect) {
		// Make sure effect doesn't exist first
		removeBaseEffect(effect);
		
		// Add effect to map
		IStateType property = effect.
		if (property != null) {
			HashMapList<IEffect, Long> list = fBaseEffects.get(property.toString());
			if (list == null) {
				list = new HashMapList<IEffect, Long>();
				fBaseEffects.put(property.toString(), list);
			}
			
			list.add(list.getSize(), effect, effect.getDuration());
		}
	}
	
	public void removeBaseEffect(IEffect effect) {
		IEffectStateType property = effect.getStateType();
		if (property != null) {
			HashMapList<IEffect, Long> list = fBaseEffects.get(property.toString());
			if (list != null) {
				list.removeByKey(effect);
				if (list.getSize() < 1) {
					fBaseEffects.remove(property.toString());
				}
			}
		}
	}
	
	public void addEffect(IEffect effect, double priority) {
		// Make sure effect doesn't exist first
		removeEffect(effect);
		
		// Add effect to map
		ComparisonDouble pri = new ComparisonDouble(priority);
		fEffectPriority.put(effect, pri);
		HashMapList<IEffect, Long> list = fEffects.get(priority);
		if (list == null) {
			list = new HashMapList<IEffect, Long>();
			fEffects.put(pri, list);
		}
		
		list.add(list.getSize(), effect, effect.getDuration());
	}
	
	public void removeEffect(IEffect effect) {
		ComparisonDouble priority = fEffectPriority.remove(effect);
		if (priority != null) {
			HashMapList<IEffect, Long> list = fEffects.get(priority);
			if (list != null) {
				list.removeByKey(effect);
				if (list.getSize() < 1) {
					fEffects.remove(priority);
				}
			}
		}
	}
	
	public void clear() {
		fBaseEffects.clear();
		fEffects.clear();
		fEffectPriority.clear();
	}
	
	public Map<String, Double> update(long time, Map<String, Double> values) {	
		// Apply Base Effects
		List<String> setsToRemove = new LinkedList<String>();
		List<IEffect> effectsToRemove = new LinkedList<IEffect>();
		for (Entry<String, HashMapList<IEffect, Long>> set : fBaseEffects.entrySet()) {
			// Find base effects to remove
			String currentKey = set.getKey();
			HashMapList<IEffect, Long> currentValue = set.getValue();
			for (Entry<IEffect, Long> effect : currentValue.iterator()) {
				Long duration = effect.getValue();
				if (duration != null) {
					if (duration - time < 0.0) {
						effectsToRemove.add(effect.getKey());
						continue;
					}
					effect.setValue(duration - time);
				}
			}
			
			// Remove those base effects
			for (IEffect effect : effectsToRemove) {
				currentValue.removeByKey(effect);
			}
			effectsToRemove.clear();
			if (currentValue.getSize() < 1) {
				setsToRemove.add(currentKey);
				continue;
			}
			
			// Update values with only the top level base effect
			if (currentKey != null) {
				Double value = values.get(currentKey);
				if (value != null) {
					IEffect effect = currentValue.getKeyByIndex(-1);
					if (effect != null) {
						values.put(currentKey, effect.computeNewValue(effect.getStateType(), value));
					}
				}
			}
		}
		
		// Remove sections of the map which are no longer useful
		for (String currentKey : setsToRemove) {
			fBaseEffects.remove(currentKey);
		}
		setsToRemove.clear();
		

		// Apply Effects
		List<ComparisonDouble> prioritiesToRemove = new LinkedList<ComparisonDouble>();
		effectsToRemove.clear();
		for (Entry<ComparisonDouble, HashMapList<IEffect, Long>> set : fEffects.entrySet()) {
			// Find effects to remove
			HashMapList<IEffect, Long> currentValue = set.getValue();
			for (Entry<IEffect, Long> effect : currentValue.iterator()) {
				Long duration = effect.getValue();
				if (duration != null) {
					if (duration - time < 0.0) {
						effectsToRemove.add(effect.getKey());
						continue;
					}
					effect.setValue(duration - time);
				}
			}
			
			// Remove those effects
			for (IEffect effect : effectsToRemove) {
				currentValue.removeByKey(effect);
				fEffectPriority.remove(effect);
			}
			effectsToRemove.clear();
			if (currentValue.getSize() < 1) {
				prioritiesToRemove.add(set.getKey());
				continue;
			}
			
			// Update values with all remaining effects
			for (Entry<IEffect, Long> effect : currentValue.iterator()) {
				IEffect currentEffect = effect.getKey();
				Double value = values.get(currentEffect.getStateType().toString());
				if (value != null) {
					values.put(currentEffect.getStateType().toString(), currentEffect.computeNewValue(currentEffect.getStateType(), value));
				}
			}
		}
		
		// Remove sections of the map which are no longer useful
		for (ComparisonDouble currentKey : prioritiesToRemove) {
			fEffects.remove(currentKey);
		}
		prioritiesToRemove.clear();
		
		return values;
	}
	
	protected static class ComparisonDouble {
		protected final double fValue;
		
		protected ComparisonDouble(double value) {
			fValue = value;
		}
		
		public boolean equals(Object paramObject)
		{
			Double value = null;
			if (paramObject instanceof ComparisonDouble) {
				value = ((ComparisonDouble) paramObject).fValue;
			} else if (paramObject instanceof Double) {
				value = (Double) paramObject;
			}
			
			if (value == null) {
				return super.equals(paramObject);
			} else {
				if (Double.isNaN(fValue) && Double.isNaN(value)) {
					return true;
				}
				return fValue == value;
			}
		}
		 
		public int compare(Double param) {
			return compare(fValue, param);
		}
		
		public int compare(ComparisonDouble param) {
			return compare(fValue, param.fValue);
		}
		
		public static int compare(Double paramT1, Double paramT2) {
			if (Double.isNaN(paramT1) || Double.isNaN(paramT2)) {
				if (Double.isNaN(paramT1)) {
					if (Double.isNaN(paramT2)) {
						return 0;
					} else if (paramT2 < 0.0) {
						return 1;
					} else {
						return -1;
					}
				} else if (Double.isNaN(paramT2)) {
					if (paramT1 < 0.0) {
						return -1;
					} else {
						return 1;
					}
				}
			} else if (Double.isInfinite(paramT1) || Double.isInfinite(paramT2)) {
				if (paramT1 == Double.NEGATIVE_INFINITY) {
					if (paramT2 == Double.NEGATIVE_INFINITY) {
						return 0;
					}
					return -1;
				} else if (paramT1 == Double.POSITIVE_INFINITY) {
					if (paramT2 == Double.POSITIVE_INFINITY) {
						return 0;
					}
					return 1;
				} else if (paramT2 == Double.NEGATIVE_INFINITY) {
					return 1;
				} else if (paramT2 == Double.POSITIVE_INFINITY) {
					return -1;
				}
			} else {
				if (paramT1 < paramT2) {
					return -1;
				} else if (paramT1 > paramT2) {
					return 1;
				} else {
					return 0;
				}
			}
			return 0;
		}
	}

	@Override
	public <V> boolean setValue(IMappable callingObject, IStateType<V> type,
			V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <V> V getValue(IStateType<V> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getLastValue(IStateType<V> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addInstantEffect(IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addInstantEffect(Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IStateType<?> getEffectType(String effectTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(String... effectTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(
			IStateType<?>... effectTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(
			Collection<Object> effectTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addBaseEffect(IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addBaseEffect(Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addConstantEffect(double priority, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addConstantEffect(double priority,
			Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTimedEffect(double priority, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTimedEffect(double priority, Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject, String... effectIDs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject,
			Collection<Object> effectIDs) {
		// TODO Auto-generated method stub
		return false;
	}
}
