package scorta;

import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class Frontend {

    public static void main(String[] args) {
        HTMLDocument doc = HTMLDocument.current();

        HTMLElement btn = doc.getElementById("loadBtn");
        HTMLElement output = doc.getElementById("output");

        if (btn != null && output != null) {
            btn.addEventListener(
                "click",
                new EventListener() {
                    @Override
                    public void handleEvent(
                        org.teavm.jso.dom.events.Event evt
                    ) {
                        output.setInnerHTML("Loading...");

                        XMLHttpRequest xhr = XMLHttpRequest.create();
                        xhr.open("GET", "/api/hello");
                        xhr.onReadyStateChange(
                            new EventListener() {
                                @Override
                                public void handleEvent(
                                    org.teavm.jso.dom.events.Event e
                                ) {
                                    if (
                                        xhr.getReadyState() ==
                                        XMLHttpRequest.DONE
                                    ) {
                                        if (xhr.getStatus() == 200) {
                                            output.setInnerHTML(
                                                xhr.getResponseText()
                                            );
                                        } else {
                                            output.setInnerHTML(
                                                "Error: " + xhr.getStatus()
                                            );
                                        }
                                    }
                                }
                            }
                        );
                        xhr.send();
                    }
                }
            );
        }
    }
}
