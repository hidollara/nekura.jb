module Music exposing
  ( Model
  , decoder
  , toTitleWithAnchor
  )

import Html exposing (..)
import Html.Attributes exposing (..)
import Json.Decode
import Json.Decode.Pipeline
import Url.Builder



type alias Model =
  { mid : Int
  , title : String
  }


decoder : Json.Decode.Decoder Model
decoder =
  Json.Decode.succeed Model
    |> Json.Decode.Pipeline.required "mid" Json.Decode.int
    |> Json.Decode.Pipeline.required "title" Json.Decode.string


toTitleWithAnchor : Model -> Html msg
toTitleWithAnchor music =
  a [ href (Url.Builder.relative [] [ Url.Builder.string "musicTitles" music.title ]) ]
    [ text music.title ]
