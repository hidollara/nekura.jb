module Record exposing
  ( Model
  , decoder
  , listToTable
  )

import Bootstrap.Table
import Html exposing (..)
import Html.Attributes exposing (..)
import Json.Decode
import Json.Decode.Extra
import Json.Decode.Pipeline
import Time
import TimeZone
import Url.Builder

import Music
import Player



type alias Model =
  { music : Music.Model
  , diff : String
  , mode : String
  , sourceUrl : String
  , player : Player.Model
  , score : Int
  , recordedAt : Time.Posix
  }


decoder : Json.Decode.Decoder Model
decoder =
  Json.Decode.succeed Model
    |> Json.Decode.Pipeline.required "rankingId" (Json.Decode.field "music" Music.decoder)
    |> Json.Decode.Pipeline.required "rankingId" (Json.Decode.field "diff" Json.Decode.string)
    |> Json.Decode.Pipeline.required "rankingId" (Json.Decode.field "mode" Json.Decode.string)
    |> Json.Decode.Pipeline.required "rankingId" (Json.Decode.field "sourceUrl" Json.Decode.string)
    |> Json.Decode.Pipeline.required "player" Player.decoder
    |> Json.Decode.Pipeline.required "score" Json.Decode.int
    |> Json.Decode.Pipeline.required "recordedAt" Json.Decode.Extra.datetime


listToTable : List Model -> Html msg
listToTable records =
  Bootstrap.Table.table
    { options =
        [ Bootstrap.Table.striped
        , Bootstrap.Table.small
        ]
    , thead =
        Bootstrap.Table.simpleThead
          [ Bootstrap.Table.th [] [ text "曲名" ]
          , Bootstrap.Table.th [] [ text "難易度" ]
          , Bootstrap.Table.th [] [ text "モード" ]
          , Bootstrap.Table.th [] [ text "プレーヤー名 (ライバルID)" ]
          , Bootstrap.Table.th [] [ text "スコア" ]
          , Bootstrap.Table.th [] [ text "達成日時" ]
          ]
    , tbody =
        Bootstrap.Table.tbody [] (List.map toTableRow records)
    }


toTableRow : Model -> Bootstrap.Table.Row msg
toTableRow record =
  Bootstrap.Table.tr []
    [ Bootstrap.Table.td []
        [ Music.toTitleWithAnchor record.music
        , a [ href record.sourceUrl ] [ text "_" ]
        ]
    , Bootstrap.Table.td [] [ text record.diff ]
    , Bootstrap.Table.td [] [ text record.mode ]
    , Bootstrap.Table.td [] [ Player.toNameWithAnchor record.player ]
    , Bootstrap.Table.td [] [ text (String.fromInt record.score) ]
    , Bootstrap.Table.td [] [ text (posixToString record.recordedAt) ]
    ]


posixToString : Time.Posix -> String
posixToString posix =
  let
    timezone = Time.utc
    year = String.fromInt (Time.toYear timezone posix)
    month = String.fromInt (monthToInt (Time.toMonth timezone posix))
    day = String.fromInt (Time.toDay timezone posix)
    hour = String.padLeft 2 '0' (String.fromInt (Time.toHour timezone posix))
    minute = String.padLeft 2 '0' (String.fromInt (Time.toMinute timezone posix))
  in
    year ++ "/" ++ month ++ "/" ++ day ++ " " ++ hour ++ ":" ++ minute


monthToInt : Time.Month -> Int
monthToInt month =
  case month of
    Time.Jan -> 1
    Time.Feb -> 2
    Time.Mar -> 3
    Time.Apr -> 4
    Time.May -> 5
    Time.Jun -> 6
    Time.Jul -> 7
    Time.Aug -> 8
    Time.Sep -> 9
    Time.Oct -> 10
    Time.Nov -> 11
    Time.Dec -> 12
