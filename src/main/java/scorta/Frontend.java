package scorta;

import org.teavm.jso.JSBody;

public class Frontend {

    public static void main(String[] args) {
        initApp();
    }

    @JSBody(
        script = """
            console.log("TeaVM Frontend init");

            const btn = document.getElementById("loadBtn");
            const output = document.getElementById("output");

            if (!btn || !output) {
                console.error("Element not found in DOM!");
                return;
            }

            btn.addEventListener("click", async () => {
                try {
                    console.log("Send request to backend...");
                    const response = await fetch("/api/hello");
                    const text = await response.text();
                    output.textContent = text;
                    console.log("Response:", text);
                } catch (err) {
                    console.error("Request error:", err);
                    output.textContent = "Server communication error";
                }
            });
        """
    )
    public static native void initApp();
}
