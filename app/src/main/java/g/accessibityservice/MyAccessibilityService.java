package g.accessibityservice;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by ganesh on 09-11-2016.
 */

public class MyAccessibilityService extends AccessibilityService
{
    public static String TAG = "MyAccessibityService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        AccessibilityNodeInfo source;
        AccessibilityNodeInfo rowNode;

        if (event.getSource() == null) {
            Log.d(MyAccessibilityService.class.getSimpleName(), "the source = null");
            return;
        }
       if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
       {
           Log.v(TAG,"editable one");

           source = event.getSource();
           /*if (source == null)
           {
               //Toast.makeText(getApplicationContext(), Arrays.toString(event.getText().toArray()).replace("[","").replace("]",""),Toast.LENGTH_SHORT).show();
               return;
           }*/
           rowNode = getListItemNodeInfo(source);


           if (rowNode == null)
           {
               return;
           }
           //looping through all views to get child that is editable
           for(int i = 0;i<rowNode.getChildCount();i++)
           {
               try
               {
                   if(rowNode.getChild(i).isEditable())
                   {
                       AccessibilityNodeInfo editNode = rowNode.getChild(i);
                       String taskLabel = editNode.getText().toString();
                       if(taskLabel.equals("Android"))
                       {
                           Bundle argumentsTest = new Bundle();
                           argumentsTest.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "Hacked");
                           editNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,argumentsTest);
                           //Android fields are getting replaced with Hacked
                           source.recycle();
                           rowNode.recycle();
                           break;
                       }
                   }
                   else
                   {
                       //Log.v(TAG,"not editable one");
                   }
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
           }
       }




    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {
            AccessibilityNodeInfo parent = current.getParent();
            if (parent == null) {
                return null;
            }
            AccessibilityNodeInfo oldCurrent = current;
            current = parent;
            oldCurrent.recycle();
            return current;
        }
    }

    @Override
    public void onServiceConnected()
    {
        Log.v(TAG,"onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED|AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt()
    {
        Log.v(TAG,"onInterrupt");
    }

}
