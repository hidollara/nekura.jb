index.min.js: index.js
	yarn run --silent uglifyjs $< --compress "pure_funcs=[F2,F3,F4,F5,F6,F7,F8,F9,A2,A3,A4,A5,A6,A7,A8,A9],pure_getters,keep_fargs=false,unsafe_comps,unsafe" | yarn run --silent uglifyjs --mangle --output=$@

index.js: src/Main.elm src/*
	yarn run --silent elm make --optimize --output=$@ $<

run:
	yarn run --silent elm-live src/Main.elm -u

clean:
	rm -f index.html index.js index.min.js
