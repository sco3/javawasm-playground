package scorta;

import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class Frontend {

    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement button = document.getElementById("loadBtn");
        HTMLElement output = document.getElementById("output");

        if (button != null) {
            button.addEventListener("click", evt -> {
                XMLHttpRequest xhr = XMLHttpRequest.create();
                xhr.open("GET", "/api/hello");
                xhr.onComplete(() -> {
                    if (xhr.getStatus() == 200) {
                        output.setInnerHTML(xhr.getResponseText());
                    }
                });
                xhr.send();
            });
        }
    }
}
