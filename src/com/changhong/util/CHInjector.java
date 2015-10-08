/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.changhong.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.changhong.annotation.CHInject;
import com.changhong.annotation.CHInjectResource;
import com.changhong.annotation.CHInjectView;

public class CHInjector
{
	private static CHInjector instance;

	private CHInjector()
	{

	}

	public static CHInjector getInstance()
	{
		if (instance == null)
		{
			instance = new CHInjector();
		}
		return instance;
	}

	public void inJectAll(final Activity activity)
	{
		// TODO Auto-generated method stub
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (final Field field : fields)
			{
				if (field.isAnnotationPresent(CHInjectView.class))
				{
					injectView(activity, field);
				} else if (field.isAnnotationPresent(CHInjectResource.class))
				{
					injectResource(activity, field);
				} else if (field.isAnnotationPresent(CHInject.class))
				{
					inject(activity, field);
				}
			}
		}
	}

	private void inject(final Activity activity, final Field field)
	{
		try
		{
			field.setAccessible(true);
			field.set(activity, field.getType().newInstance());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void injectView(final Activity activity, final Field field)
	{
		final CHInjectView viewInject = field.getAnnotation(CHInjectView.class);
		if (viewInject != null)
		{
			int viewId = viewInject.id();
			try
			{
				field.setAccessible(true);
				//cxp进行ViewGroup的注入
				if(ViewGroup.class.isAssignableFrom(field.getType())){
					ViewGroup view = (ViewGroup)activity.findViewById(viewId);
					field.set(activity, view);
					injectView(view);
				}else{
					field.set(activity, activity.findViewById(viewId));
				}
			} catch (Exception e)
			{
				CHLogger.e(this, activity + "->" + field);
				e.printStackTrace();
			}
		}
	}
	
	private void injectView(final ViewGroup contentView, final Field field)
	{
		final CHInjectView viewInject = field.getAnnotation(CHInjectView.class);
		if (viewInject != null)
		{
			int viewId = viewInject.id();
			try
			{
				field.setAccessible(true);
				field.set(contentView, contentView.findViewById(viewId));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void injectResource(final Activity activity, final Field field)
	{
        final CHInjectResource resourceJect = field
                .getAnnotation(CHInjectResource.class);
		if (resourceJect != null)//field.isAnnotationPresent(CHInjectResource.class)
		{
            //TODO:API1和API14的实现不一致
            final int resourceID = resourceJect.id();
			try
			{
				field.setAccessible(true);
                final Resources resources = activity.getResources();
				final String type = resources.getResourceTypeName(resourceID);
				if (type.equalsIgnoreCase("string"))
				{
					field.set(activity,
							resources.getString(resourceID));
				} else if (type.equalsIgnoreCase("drawable"))
				{
					field.set(activity,
							resources.getDrawable(resourceID));
				} else if (type.equalsIgnoreCase("layout"))
				{
					field.set(activity,
							resources.getLayout(resourceID));
				} else if (type.equalsIgnoreCase("array"))
				{
					if (field.getType().equals(int[].class))
					{
						field.set(activity, resources
								.getIntArray(resourceID));
					} else if (field.getType().equals(String[].class))
					{
						field.set(activity, resources
								.getStringArray(resourceID));
					} else
					{
						field.set(activity, resources
								.getStringArray(resourceID));
					}

				} else if (type.equalsIgnoreCase("color"))
				{
					if (field.getType().equals(Integer.TYPE))
					{
						field.set(activity,
								resources.getColor(resourceID));
					} else
					{
						field.set(activity, resources
								.getColorStateList(resourceID));
					}

				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void inject(final Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (final Field field : fields)
			{
				if (field.isAnnotationPresent(CHInject.class))
				{
					inject(activity, field);
				}
			}
		}
	}

	public void injectView(final Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (final Field field : fields)
			{
				if (field.isAnnotationPresent(CHInjectView.class))
				{
					injectView(activity, field);
				}
			}
		}
	}
	
	public void injectView(final ViewGroup contentView)
	{
		Field[] fields = contentView.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (final Field field : fields)
			{
				if (field.isAnnotationPresent(CHInjectView.class))
				{
					injectView(contentView, field);
				}
			}
		}
	}

	public void injectResource(final Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (final Field field : fields)
			{
				if (field.isAnnotationPresent(CHInjectResource.class))
				{
					injectResource(activity, field);
				}
			}
		}
	}

}
